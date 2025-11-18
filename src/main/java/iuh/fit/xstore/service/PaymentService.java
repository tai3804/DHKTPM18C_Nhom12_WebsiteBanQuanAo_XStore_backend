package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.request.CheckoutRequest;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Service xử lý thanh toán và tạo đơn hàng
 */
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PaymentService {
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final DiscountRepository discountRepo;

    /**
     * Xử lý checkout và tạo đơn hàng
     */
    public Order processCheckout(CheckoutRequest request) {
        log.info("🔵 Processing checkout for user: {}", request.getUserId());

        // 0. Validate request
        if (request == null || request.getUserId() <= 0 || request.getCartId() <= 0) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getShippingAddress() == null || request.getShippingAddress().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        if (request.getPhoneNumber() == null || request.getPhoneNumber().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // 1. Kiểm tra người dùng tồn tại
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Kiểm tra giỏ hàng
        Cart cart = cartRepo.findById(request.getCartId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // 3. Tạo Order
        Order order = Order.builder()
                .user(user)
                .createdAt(LocalDate.now())
                .status(OrderStatus.PENDING)
                .paymentMethod(request.getPaymentMethod() != null ? 
                        request.getPaymentMethod() : "CASH")
                .shippingAddress(request.getShippingAddress())
                .phoneNumber(request.getPhoneNumber())
                .recipientName(request.getRecipientName())
                .notes(request.getNotes() != null ? request.getNotes() : "")
                .build();

        // 4. Tạo OrderItems từ CartItems
        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem == null || cartItem.getProduct() == null) {
                log.warn("⚠️ Found null cart item, skipping");
                continue;
            }

            Product product = cartItem.getProduct();

            // Kiểm tra stock (sử dụng priceInStock như tên field)
            if (product.getPriceInStock() < cartItem.getQuantity()) {
                log.error("❌ Insufficient stock for product: {} (Available: {}, Requested: {})", 
                        product.getId(), product.getPriceInStock(), cartItem.getQuantity());
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            // Tính giá unit (để lưu giá tại thời điểm mua)
            double unitPrice = product.getPrice();
            double itemSubtotal = unitPrice * cartItem.getQuantity();

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .subTotal(itemSubtotal)
                    .color(cartItem.getProductInfo() != null ? cartItem.getProductInfo().getColorName() : "")
                    .size(cartItem.getProductInfo() != null ? cartItem.getProductInfo().getSizeName() : "")
                    .build();

            orderItems.add(orderItem);
            subtotal += itemSubtotal;

            // Giảm tồn kho
            product.setPriceInStock(product.getPriceInStock() - cartItem.getQuantity());
            productRepo.save(product);
            log.debug("📦 Stock updated for product {}: {} -> {}", 
                    product.getId(), product.getPriceInStock() + cartItem.getQuantity(), 
                    product.getPriceInStock());
        }

        if (orderItems.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        order.setOrderItems(orderItems);

        // 5. Xử lý giảm giá từ danh sách discountIds (tối đa 3 cho hoá đơn)
        double discountAmount = 0;
        
        if (request.getDiscountIds() != null && !request.getDiscountIds().isEmpty()) {
            // Giới hạn tối đa 3 mã giảm giá
            List<Integer> validDiscountIds = request.getDiscountIds().stream()
                    .filter(id -> id != null && id > 0)
                    .distinct()
                    .limit(3)
                    .toList();
            
            for (Integer discountId : validDiscountIds) {
                try {
                    Discount discount = discountRepo.findById(discountId)
                            .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
                    
                    // Kiểm tra discount có hợp lệ không với user hiện tại
                    if (!discount.isValidForUser(user)) {
                        log.warn("⚠️ Discount code {} is invalid, expired, or not applicable for user type: {}", discountId, discount.getId());
                        continue; // Bỏ qua discount không hợp lệ
                    }
                    
                    // Chỉ áp dụng discount cho PRODUCT (hoá đơn), không cho SHIPPING
                    if (!"PRODUCT".equals(discount.getCategory()) && !"SHIPPING".equals(discount.getCategory())) {
                        // Nếu category null hoặc khác, mặc định coi là PRODUCT
                        if (discount.getCategory() != null && !"PRODUCT".equals(discount.getCategory())) {
                            log.warn("⚠️ Discount {} is for {}, skipping for product discount", discountId, discount.getCategory());
                            continue;
                        }
                    }
                    
                    // Tính giảm giá: lấy min giữa discount fixed amount và discount percent
                    double currentDiscount = Math.min(
                            discount.getDiscountAmount(),
                            (long)(subtotal * discount.getDiscountPercent() / 100)
                    );
                    
                    discountAmount += currentDiscount;
                    
                    // Tăng số lần sử dụng
                    discount.setUsageCount(discount.getUsageCount() + 1);
                    discountRepo.save(discount);
                    
                    log.info("✅ Applied discount: {} (Amount: {}₫)", discount.getName(), currentDiscount);
                    
                } catch (Exception e) {
                    log.warn("⚠️ Error processing discount {}: {}", discountId, e.getMessage());
                    // Tiếp tục với discount tiếp theo
                }
            }
            
            // Đảm bảo tổng giảm giá không vượt quá subtotal
            discountAmount = Math.min(discountAmount, subtotal);
            
            log.info("✅ Total discount applied from {} codes: {}₫", validDiscountIds.size(), discountAmount);
        }

        // 6. Tính phí vận chuyển
        double shippingFee = calculateShippingFee(request.getShippingAddress());
        if (shippingFee < 0) {
            shippingFee = 0;
        }

        // Áp dụng giảm giá phí vận chuyển nếu có
        if (request.getShippingDiscountId() != null && request.getShippingDiscountId() > 0) {
            Discount shippingDiscount = discountRepo.findById(request.getShippingDiscountId())
                    .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));

            // Kiểm tra discount có hợp lệ không
            if (!shippingDiscount.isValidForUser(user)) {
                log.warn("⚠️ Shipping discount is invalid for user: {}", shippingDiscount.getId());
                throw new AppException(ErrorCode.INVALID_DISCOUNT);
            }

            // Tính giảm giá cho shipping
            double shippingDiscountAmount = 0;
            if (shippingDiscount.getType().equals("PERCENTAGE")) {
                shippingDiscountAmount = shippingFee * (shippingDiscount.getDiscountPercent() / 100);
                if (shippingDiscount.getDiscountAmount() > 0 &&
                    shippingDiscountAmount > shippingDiscount.getDiscountAmount()) {
                    shippingDiscountAmount = shippingDiscount.getDiscountAmount();
                }
            } else {
                shippingDiscountAmount = Math.min(shippingDiscount.getDiscountAmount(), shippingFee);
            }

            shippingFee -= shippingDiscountAmount;
            if (shippingFee < 0) shippingFee = 0;

            // Tăng số lần sử dụng
            shippingDiscount.setUsageCount(shippingDiscount.getUsageCount() + 1);
            discountRepo.save(shippingDiscount);
            log.info("✅ Shipping discount applied: {} (Amount: {}₫)", shippingDiscount.getId(), shippingDiscountAmount);
        }

        // 7. Tính tổng tiền
        double total = subtotal - discountAmount + shippingFee;
        if (total < 0) {
            total = 0;
        }

        order.setSubtotal(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setShippingFee(shippingFee);
        order.setTotal(total);

        // 8. Lưu đơn hàng
        Order savedOrder = orderRepo.save(order);
        log.info("✅ Order created successfully: Order #{} | Subtotal: {}₫ | Discount: {}₫ | Shipping: {}₫ | Total: {}₫", 
                savedOrder.getId(), subtotal, discountAmount, shippingFee, total);

        // 9. Xóa giỏ hàng
        clearCart(request.getCartId());

        return savedOrder;
    }

    /**
     * Tính phí vận chuyển dựa trên địa chỉ
     * Quy tắc: 
     * - TP.HCM = 25.000₫
     * - Ngoài TP.HCM = 40.000₫
     */
    private double calculateShippingFee(String address) {
        if (address == null || address.trim().isEmpty()) {
            return 40000; // Default shipping fee
        }

        String addressUpper = address.toUpperCase();
        
        // Kiểm tra các biến thể của TP.HCM
        if (addressUpper.contains("TP.HCM") || 
            addressUpper.contains("TPHCM") || 
            addressUpper.contains("HCM") ||
            addressUpper.contains("TP HO CHI MINH") ||
            addressUpper.contains("HO CHI MINH")) {
            return 25000;
        }

        // Các tỉnh khác
        return 40000;
    }

    /**
     * Xóa giỏ hàng sau khi checkout thành công
     */
    private void clearCart(int cartId) {
        try {
            Cart cart = cartRepo.findById(cartId)
                    .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
            
            // Xóa tất cả items bằng cách clear collection và save
            // orphanRemoval = true sẽ tự động delete các items
            cart.getCartItems().clear();
            cartRepo.save(cart);
            
            log.info("✅ Cart cleared successfully: {}", cartId);
        } catch (Exception e) {
            log.error("❌ Error clearing cart {}: {}", cartId, e.getMessage());
            // Không throw exception, chỉ log warning
        }
    }

    /**
     * Xử lý thanh toán qua các cổng khác nhau
     * Hiện tại chỉ hỗ trợ CASH (thanh toán khi nhận hàng)
     */
    public boolean processPayment(Order order, String paymentMethod) {
        if (order == null) {
            log.error("❌ Order is null");
            return false;
        }

        String method = (paymentMethod != null) ? paymentMethod.toUpperCase() : "CASH";
        log.info("💳 Processing payment - Method: {}, Order: {}", method, order.getId());

        try {
            switch (method) {
                case "CASH":
                    return processCashPayment(order);
                
                case "CARD":
                    log.warn("⚠️ CARD payment is not yet implemented");
                    return processCardPayment(order);
                
                case "MOMO":
                    log.warn("⚠️ MOMO payment is not yet implemented");
                    return processMomoPayment(order);
                
                case "ZALOPAY":
                    log.warn("⚠️ ZALOPAY payment is not yet implemented");
                    return processZaloPayPayment(order);
                
                default:
                    log.error("❌ Unknown payment method: {}", method);
                    throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } catch (Exception e) {
            log.error("❌ Error processing payment: {}", e.getMessage(), e);
            order.setStatus(OrderStatus.PENDING);
            orderRepo.save(order);
            return false;
        }
    }

    /**
     * Xử lý thanh toán tiền mặt (CASH)
     * Đơn hàng sẽ được xác nhận ngay
     */
    private boolean processCashPayment(Order order) {
        try {
            log.info("💵 Processing CASH payment for order: {}", order.getId());
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);
            log.info("✅ CASH payment confirmed for order: {}", order.getId());
            return true;
        } catch (Exception e) {
            log.error("❌ Error processing CASH payment: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Xử lý thanh toán thẻ (CARD)
     * TODO: Integrate với Stripe, PayPal, v.v
     */
    private boolean processCardPayment(Order order) {
        try {
            log.info("💳 Processing CARD payment for order: {}", order.getId());
            // TODO: Integrate với payment gateway
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);
            log.info("✅ CARD payment processed for order: {}", order.getId());
            return true;
        } catch (Exception e) {
            log.error("❌ Error processing CARD payment: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Xử lý thanh toán MoMo
     * TODO: Integrate với MoMo API
     */
    private boolean processMomoPayment(Order order) {
        try {
            log.info("📱 Processing MOMO payment for order: {}", order.getId());
            // TODO: Integrate với MoMo payment gateway
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);
            log.info("✅ MOMO payment processed for order: {}", order.getId());
            return true;
        } catch (Exception e) {
            log.error("❌ Error processing MOMO payment: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Xử lý thanh toán ZaloPay
     * TODO: Integrate với ZaloPay API
     */
    private boolean processZaloPayPayment(Order order) {
        try {
            log.info("📱 Processing ZALOPAY payment for order: {}", order.getId());
            // TODO: Integrate với ZaloPay payment gateway
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);
            log.info("✅ ZALOPAY payment processed for order: {}", order.getId());
            return true;
        } catch (Exception e) {
            log.error("❌ Error processing ZALOPAY payment: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Lấy danh sách đơn hàng của user
     * Sử dụng query repository để hiệu quả hơn
     */
    public List<Order> getUserOrders(int userId) {
        // Kiểm tra user tồn tại
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        log.info("📋 Fetching orders for user: {}", userId);
        
        // Nếu có method findByUser trong repository, sử dụng nó
        // return orderRepo.findByUser(user);
        
        // Nếu không, sử dụng stream filter
        List<Order> orders = orderRepo.findAll().stream()
                .filter(order -> order.getUser().getId() == userId)
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .toList();

        log.info("✅ Found {} orders for user: {}", orders.size(), userId);
        return orders;
    }

    /**
     * Hủy đơn hàng
     * - Chỉ hủy được đơn hàng ở trạng thái PENDING hoặc CONFIRMED
     * - Hoàn lại tồn kho (priceInStock)
     * - Ghi log reason
     */
    @Transactional
    public void cancelOrder(int orderId, String reason) {
        log.info("🚨 Cancelling order: {}, Reason: {}", orderId, reason);

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Kiểm tra trạng thái
        if (!order.getStatus().equals(OrderStatus.PENDING) &&
            !order.getStatus().equals(OrderStatus.CONFIRMED)) {
            log.error("❌ Cannot cancel order {} with status: {}", orderId, order.getStatus());
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        // Hoàn lại stock cho tất cả items
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for (OrderItem item : order.getOrderItems()) {
                if (item == null || item.getProduct() == null) {
                    log.warn("⚠️ Found null order item, skipping");
                    continue;
                }

                Product product = item.getProduct();
                product.setPriceInStock(product.getPriceInStock() + item.getQuantity());
                productRepo.save(product);
                
                log.debug("📦 Stock restored for product {}: +{}", 
                        product.getId(), item.getQuantity());
            }
        }

        // Cập nhật trạng thái
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);

        log.info("✅ Order cancelled successfully: {}", orderId);
    }
}
