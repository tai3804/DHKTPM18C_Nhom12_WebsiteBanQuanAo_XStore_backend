package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Request;
import iuh.fit.xstore.model.RequestStatus;
import iuh.fit.xstore.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    // ========== USER REQUESTS ==========
    @PostMapping
    public ApiResponse<Request> createRequest(@RequestBody Request request) {
        var created = requestService.createRequest(request);
        return new ApiResponse<>(SuccessCode.REQUEST_CREATED.getCode(),
                "Yêu cầu đã được gửi thành công.", created);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<Request>> getUserRequests(@PathVariable int userId) {
        var requests = requestService.findRequestsByUser(userId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), requests);
    }

    @GetMapping
    public ApiResponse<List<Request>> getRequests(@RequestParam(required = false) Integer orderId) {
        List<Request> requests;
        if (orderId != null) {
            requests = requestService.findRequestsByOrder(orderId);
        } else {
            requests = requestService.findAllRequests();
        }
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), requests);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ApiResponse<List<Request>> getAllRequests() {
        var requests = requestService.findAllRequests();
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), requests);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<Request> getRequestById(@PathVariable int id) {
        var request = requestService.findRequestById(id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ApiResponse<Request> updateRequestStatus(
            @PathVariable int id,
            @RequestParam RequestStatus status,
            @RequestParam(defaultValue = "") String adminNote) {
        var updated = requestService.updateRequestStatus(id, status, adminNote);
        return new ApiResponse<>(SuccessCode.REQUEST_UPDATED.getCode(),
                "Yêu cầu đã được cập nhật.", updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ApiResponse<Map<String, Object>> getRequestStats() {
        Map<String, Object> stats = Map.of(
                "totalRequests", requestService.getTotalRequests(),
                "pendingRequests", requestService.getPendingRequests(),
                "approvedRequests", requestService.getApprovedRequests(),
                "rejectedRequests", requestService.getRejectedRequests()
        );
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), stats);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ApiResponse<List<Request>> searchRequests(@RequestParam String keyword) {
        var requests = requestService.searchRequests(keyword);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS.getCode(),
                SuccessCode.FETCH_SUCCESS.getMessage(), requests);
    }
}