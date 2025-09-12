package xstore.model;

/**
 * Copyright (c) 2025 by Tai.
 * All rights reserved.
 * This file is part of X-Store.
 */

public enum OrderStatus {
    IN_TRANSIT,          // Đang giao hàng
    PENDING_RECEIPT,     // Chờ nhận hàng
    DELIVERED,           // Đã giao hàng
    CANCELLED,           // Đã hủy
    RETURN_REQUESTED     // Yêu cầu đổi/trả
}
