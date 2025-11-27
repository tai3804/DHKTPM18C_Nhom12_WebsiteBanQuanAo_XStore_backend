package iuh.fit.xstore.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import iuh.fit.xstore.model.Order;
import iuh.fit.xstore.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PdfService {

    private PdfFont font;

    public byte[] generateOrderPdf(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không được null");
        }

        System.out.println("Bắt đầu tạo PDF cho đơn hàng ID: " + order.getId());

        // Load font Unicode hỗ trợ tiếng Việt
        try {
            String fontPath = getClass().getResource("/fonts/Roboto-Regular.ttf").getPath();
            font = PdfFontFactory.createFont(fontPath, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            System.out.println("Đã tải font Roboto thành công từ resources");
        } catch (Exception e) {
            System.err.println("Không thể tải font Roboto từ resources: " + e.getMessage());
            // Fallback: thử dùng font Arial có sẵn trong hệ thống Windows
            try {
                font = PdfFontFactory.createFont("C:/Windows/Fonts/arial.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
                System.out.println("Đã tải font Arial từ Windows Fonts");
            } catch (Exception ex) {
                System.err.println("Không thể tải font Arial: " + ex.getMessage());
                // Fallback cuối cùng: dùng font mặc định
                try {
                    font = PdfFontFactory.createFont();
                    System.out.println("Đang dùng font mặc định của iText");
                } catch (Exception ex2) {
                    throw new RuntimeException("Không thể khởi tạo font", ex2);
                }
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            // Set document properties for Unicode support
            pdfDoc.getCatalog().setLang(new com.itextpdf.kernel.pdf.PdfString("vi-VN"));
            Document document = new Document(pdfDoc);

            // Set font mặc định cho toàn bộ document
            document.setFont(font);
            document.setFontSize(11);

            // Set page margins
            document.setMargins(50, 50, 50, 50);

            // Title
            Paragraph title = new Paragraph("HÓA ĐƠN ĐẶT HÀNG")
                    .setFont(font)
                    .setFontSize(22)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontColor(new DeviceRgb(70, 130, 180)) // Steel blue color
                    .setMarginBottom(25);
            document.add(title);

            // Store Info
            Paragraph storeInfo = new Paragraph("X-STORE - Thời trang chất lượng cao")
                    .setFont(font)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(100, 100, 100)) // Dark gray
                    .setMarginBottom(15);
            document.add(storeInfo);

            Paragraph storeAddress = new Paragraph("Địa chỉ: 123 Đường ABC, Quận 1, TP.HCM\nEmail: support@xstore.com | Hotline: 0123 456 789")
                    .setFont(font)
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(120, 120, 120)) // Medium gray
                    .setMarginBottom(25);
            document.add(storeAddress);

            // Add a decorative line
            Table separator = new Table(UnitValue.createPercentArray(new float[]{1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(30);
            Cell separatorCell = new Cell();
            separatorCell.setBorderTop(new SolidBorder(new DeviceRgb(70, 130, 180), 2)); // Blue line
            separatorCell.setBorderBottom(Border.NO_BORDER);
            separatorCell.setBorderLeft(Border.NO_BORDER);
            separatorCell.setBorderRight(Border.NO_BORDER);
            separatorCell.setHeight(1);
            separator.addCell(separatorCell);
            document.add(separator);

            // Order Info
            Table orderInfoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            orderInfoTable.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1));

            orderInfoTable.addCell(createCell("Mã đơn hàng:", true));
            orderInfoTable.addCell(createCell(String.valueOf(order.getId()), false));

            orderInfoTable.addCell(createCell("Ngày đặt:", true));
            String createdDate = order.getCreatedAt() != null
                ? order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "N/A";
            orderInfoTable.addCell(createCell(createdDate, false));

            orderInfoTable.addCell(createCell("Trạng thái:", true));
            String statusText = order.getStatus() != null
                ? getStatusText(order.getStatus().toString())
                : "N/A";
            orderInfoTable.addCell(createCell(statusText, false));

            document.add(orderInfoTable);

            // Customer Info
            Paragraph customerTitle = new Paragraph("THÔNG TIN KHÁCH HÀNG")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(new DeviceRgb(70, 130, 180)) // Steel blue
                    .setMarginBottom(15);
            document.add(customerTitle);

            Table customerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            customerTable.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1));

            customerTable.addCell(createCell("Người nhận:", true));
            String recipientName = order.getRecipientName() != null && !order.getRecipientName().trim().isEmpty()
                ? order.getRecipientName().trim()
                : (order.getUser() != null && order.getUser().getFirstName() != null && !order.getUser().getFirstName().trim().isEmpty()
                    ? order.getUser().getFirstName().trim()
                    : "N/A");
            customerTable.addCell(createCell(recipientName, false));

            customerTable.addCell(createCell("Số điện thoại:", true));
            String phone = order.getPhoneNumber() != null && !order.getPhoneNumber().trim().isEmpty()
                ? order.getPhoneNumber().trim()
                : (order.getUser() != null && order.getUser().getPhone() != null && !order.getUser().getPhone().trim().isEmpty()
                    ? order.getUser().getPhone().trim()
                    : "N/A");
            customerTable.addCell(createCell(phone, false));

            customerTable.addCell(createCell("Email:", true));
            String email = order.getUser() != null && order.getUser().getEmail() != null && !order.getUser().getEmail().trim().isEmpty()
                ? order.getUser().getEmail().trim()
                : "N/A";
            customerTable.addCell(createCell(email, false));

            customerTable.addCell(createCell("Địa chỉ giao hàng:", true));
            String address = order.getShippingAddress() != null && !order.getShippingAddress().trim().isEmpty()
                ? order.getShippingAddress().trim()
                : "N/A";
            customerTable.addCell(createCell(address, false));

            document.add(customerTable);

            // Order Items
            Paragraph itemsTitle = new Paragraph("CHI TIẾT SẢN PHẨM")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(new DeviceRgb(70, 130, 180)) // Steel blue
                    .setMarginBottom(15);
            document.add(itemsTitle);

            Table itemsTable = new Table(UnitValue.createPercentArray(new float[]{3, 1, 1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            itemsTable.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1));

            // Header
            itemsTable.addHeaderCell(createHeaderCell("Sản phẩm"));
            itemsTable.addHeaderCell(createHeaderCell("SL"));
            itemsTable.addHeaderCell(createHeaderCell("Đơn giá"));
            itemsTable.addHeaderCell(createHeaderCell("Thành tiền"));

            // Items
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item == null || item.getProduct() == null) continue;

                    String productName = item.getProduct().getName() != null
                        ? item.getProduct().getName()
                        : "Sản phẩm không xác định";

                    if (item.getColor() != null || item.getSize() != null) {
                        String variant = "";
                        if (item.getColor() != null) variant += "Màu: " + item.getColor();
                        if (item.getSize() != null) variant += (variant.isEmpty() ? "" : ", ") + "Size: " + item.getSize();
                        productName += " (" + variant + ")";
                    }

                    itemsTable.addCell(createCell(productName, false));
                    itemsTable.addCell(createCell(String.valueOf(item.getQuantity()), false));
                    itemsTable.addCell(createCell(formatCurrency(item.getUnitPrice()), false));
                    itemsTable.addCell(createCell(formatCurrency(item.getSubTotal()), false));
                }
            } else {
                // No items
                itemsTable.addCell(createCell("Không có sản phẩm", false).setItalic());
                itemsTable.addCell(createCell("", false));
                itemsTable.addCell(createCell("", false));
                itemsTable.addCell(createCell("", false));
            }

            document.add(itemsTable);

            // Order Summary
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    .setMarginBottom(20);
            summaryTable.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1));

            summaryTable.addCell(createCell("Tạm tính:", true));
            summaryTable.addCell(createCell(formatCurrency(order.getSubtotal()), false));

            if (order.getDiscountAmount() > 0) {
                summaryTable.addCell(createCell("Giảm giá:", true));
                summaryTable.addCell(createCell("-" + formatCurrency(order.getDiscountAmount()), false));
            }

            summaryTable.addCell(createCell("Phí vận chuyển:", true));
            summaryTable.addCell(createCell(formatCurrency(order.getShippingFee()), false));

            summaryTable.addCell(createTotalLabelCell("Tổng cộng:"));
            summaryTable.addCell(createTotalValueCell(formatCurrency(order.getTotal())));

            document.add(summaryTable);

            // Payment Info
            Paragraph paymentTitle = new Paragraph("THÔNG TIN THANH TOÁN")
                    .setFont(font)
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(new DeviceRgb(70, 130, 180)) // Steel blue
                    .setMarginBottom(15);
            document.add(paymentTitle);

            Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            paymentTable.setBorder(new SolidBorder(new DeviceRgb(200, 200, 200), 1));

            paymentTable.addCell(createCell("Phương thức thanh toán:", true));
            String paymentMethod = getPaymentMethodText(order.getPaymentMethod());
            paymentTable.addCell(createCell(paymentMethod, false));

            document.add(paymentTable);

            // Footer
            Paragraph footer = new Paragraph("Cảm ơn quý khách đã mua hàng tại X-Store!\nHàng sẽ được giao trong vòng 3-5 ngày làm việc.")
                    .setFont(font)
                    .setFontSize(11)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(new DeviceRgb(100, 100, 100)) // Dark gray
                    .setMarginTop(35);
            document.add(footer);

            document.close();

            System.out.println("PDF được tạo thành công cho đơn hàng ID: " + order.getId());
            return outputStream.toByteArray();

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo PDF cho đơn hàng " + order.getId() + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi tạo PDF: " + e.getMessage(), e);
        }
    }

    private Cell createCell(String content, boolean isHeader) {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(content).setFont(font);

        if (isHeader) {
            paragraph.setBold();
            cell.setBackgroundColor(new DeviceRgb(240, 240, 240)); // Light gray background for headers
        }

        cell.add(paragraph);
        cell.setPadding(8); // Increased padding
        cell.setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 0.5f)); // Light border
        return cell;
    }

    private Cell createHeaderCell(String content) {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(content).setFont(font).setBold();
        cell.add(paragraph);
        cell.setBackgroundColor(new DeviceRgb(70, 130, 180)); // Steel blue background
        cell.setPadding(10); // More padding for headers
        cell.setBorder(new SolidBorder(new DeviceRgb(100, 149, 237), 1)); // Cornflower blue border
        paragraph.setFontColor(ColorConstants.WHITE); // White text on blue background
        return cell;
    }

    private Cell createTotalLabelCell(String content) {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(content).setFont(font).setBold()
                .setFontColor(new DeviceRgb(70, 130, 180)); // Steel blue
        cell.add(paragraph);
        cell.setPadding(8);
        cell.setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 0.5f));
        cell.setBackgroundColor(new DeviceRgb(250, 250, 250)); // Very light gray
        return cell;
    }

    private Cell createTotalValueCell(String content) {
        Cell cell = new Cell();
        Paragraph paragraph = new Paragraph(content).setFont(font).setBold()
                .setFontSize(14)
                .setFontColor(new DeviceRgb(34, 139, 34)); // Forest green
        cell.add(paragraph);
        cell.setPadding(8);
        cell.setBorder(new SolidBorder(new DeviceRgb(220, 220, 220), 0.5f));
        cell.setBackgroundColor(new DeviceRgb(250, 250, 250)); // Very light gray
        return cell;
    }

    private String formatCurrency(double amount) {
        return String.format("%,.0f ₫", amount);
    }

    private String getStatusText(String status) {
        return switch (status.toUpperCase()) {
            case "PENDING" -> "Chờ xác nhận";
            case "AWAITING_PAYMENT" -> "Chờ thanh toán";
            case "CONFIRMED" -> "Đã xác nhận";
            case "PROCESSING" -> "Đang xử lý";
            case "IN_TRANSIT" -> "Đang giao hàng";
            case "PENDING_RECEIPT" -> "Chờ nhận hàng";
            case "DELIVERED" -> "Đã giao hàng";
            case "CANCELLED" -> "Đã hủy";
            case "RETURN_REQUESTED" -> "Yêu cầu đổi/trả";
            default -> status;
        };
    }

    private String getPaymentMethodText(String method) {
        if (method == null) return "Chưa xác định";

        return switch (method.toUpperCase()) {
            case "CASH" -> "Thanh toán khi nhận hàng";
            case "CARD" -> "Thẻ tín dụng";
            case "MOMO" -> "Ví MoMo";
            case "ZALOPAY" -> "ZaloPay";
            default -> method;
        };
    }
}