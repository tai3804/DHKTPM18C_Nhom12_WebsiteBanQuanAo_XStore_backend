package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.RequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepo;
    private final OrderService orderService;

    public List<Request> findAllRequests() {
        return requestRepo.findAll();
    }

    public Request findRequestById(int id) {
        return requestRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
    }

    public List<Request> findRequestsByUser(int userId) {
        return requestRepo.findByUserId(userId);
    }

    public List<Request> findRequestsByOrder(int orderId) {
        return requestRepo.findByOrderId(orderId);
    }

    @Transactional
    public Request createRequest(Request request) {
        // Kiểm tra đơn hàng tồn tại
        Order order = orderService.findOrderById(request.getOrder().getId());
        if (order == null) {
            throw new AppException(ErrorCode.ORDER_NOT_FOUND);
        }

        // Kiểm tra trạng thái đơn hàng phù hợp
        if (request.getType() == RequestType.CANCEL) {
            if (order.getStatus() != OrderStatus.PENDING &&
                order.getStatus() != OrderStatus.CONFIRMED &&
                order.getStatus() != OrderStatus.AWAITING_PAYMENT) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        } else if (request.getType() == RequestType.RETURN) {
            if (order.getStatus() != OrderStatus.DELIVERED) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
        }

        // Kiểm tra không có yêu cầu đang chờ xử lý cho đơn hàng này
        List<Request> existingRequests = requestRepo.findByOrderId(order.getId());
        boolean hasPending = existingRequests.stream()
                .anyMatch(r -> r.getStatus() == RequestStatus.PENDING);
        if (hasPending) {
            throw new AppException(ErrorCode.REQUEST_ALREADY_EXISTS);
        }

        request.setCreatedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        return requestRepo.save(request);
    }

    @Transactional
    public Request updateRequestStatus(int id, RequestStatus status, String adminNote) {
        Request request = findRequestById(id);
        request.setStatus(status);
        request.setProcessedAt(LocalDateTime.now());
        request.setAdminNote(adminNote);

        // Nếu chấp nhận yêu cầu hủy, cập nhật trạng thái đơn hàng
        if (status == RequestStatus.APPROVED && request.getType() == RequestType.CANCEL) {
            orderService.updateOrderStatus(request.getOrder().getId(), "CANCELLED");
        }
        // Nếu chấp nhận yêu cầu đổi/trả, có thể cập nhật trạng thái đơn hàng thành RETURN_REQUESTED
        else if (status == RequestStatus.APPROVED && request.getType() == RequestType.RETURN) {
            orderService.updateOrderStatus(request.getOrder().getId(), "RETURN_REQUESTED");
        }

        return requestRepo.save(request);
    }

    public long getTotalCancelRequests() {
        return requestRepo.countByType(RequestType.CANCEL);
    }

    public long getCancelRequestsThisMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return requestRepo.countByTypeAndCreatedAtAfter(RequestType.CANCEL, startOfMonth);
    }

    public long getCancelRequestsToday() {
        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return requestRepo.countByTypeAndCreatedAtAfter(RequestType.CANCEL, startOfDay);
    }

    public long getCancelRequestsThisYear() {
        LocalDateTime startOfYear = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        return requestRepo.countByTypeAndCreatedAtAfter(RequestType.CANCEL, startOfYear);
    }

    public long getTotalRequests() {
        return requestRepo.count();
    }

    public long getPendingRequests() {
        return requestRepo.countByStatus(RequestStatus.PENDING);
    }

    public long getApprovedRequests() {
        return requestRepo.countByStatus(RequestStatus.APPROVED);
    }

    public long getRejectedRequests() {
        return requestRepo.countByStatus(RequestStatus.REJECTED);
    }

    public List<Request> searchRequests(String keyword) {
        // Simple search by reason or adminNote containing keyword
        return requestRepo.findAll().stream()
                .filter(r -> (r.getReason() != null && r.getReason().contains(keyword)) ||
                            (r.getAdminNote() != null && r.getAdminNote().contains(keyword)))
                .toList();
    }
}