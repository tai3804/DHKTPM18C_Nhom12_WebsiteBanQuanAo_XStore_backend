package iuh.fit.xstore.model;

import jakarta.persistence.Entity;

public enum OrderStatus {
    PENDING,             // Chờ xác nhận
    AWAITING_PAYMENT,    // Chờ thanh toán
    CONFIRMED,           // Đã xác nhận
    PROCESSING,          // Đang xử lý
    IN_TRANSIT,          // Đang giao hàng
    PENDING_RECEIPT,     // Chờ nhận hàng
    DELIVERED,           // Đã giao hàng
    CANCELLED,           // Đã hủy
    RETURN_REQUESTED     // Yêu cầu đổi/trả
}
