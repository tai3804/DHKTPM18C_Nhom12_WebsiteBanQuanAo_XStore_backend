package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.service.ProductInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ProductInfoController - REST API cho ProductInfo
 */
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductInfoController {
    
    private final ProductInfoService productInfoService;

    /**
     * GET /api/products/{productId}/info - Lấy tất cả product info của sản phẩm
     */
    @GetMapping("/{productId}/info")
    public ResponseEntity<?> getProductInfoByProductId(@PathVariable int productId) {
        try {
            List<ProductInfo> productInfoList = productInfoService.findByProductId(productId);
            
            ApiResponse<List<ProductInfo>> response = new ApiResponse<>(
                    200,
                    "Lấy thông tin biến thể sản phẩm thành công",
                    productInfoList
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/{productId}/colors - Lấy danh sách màu sắc của sản phẩm
     */
    @GetMapping("/{productId}/colors")
    public ResponseEntity<?> getProductColors(@PathVariable int productId) {
        try {
            List<Map<String, String>> colors = productInfoService.getDistinctColors(productId);
            
            ApiResponse<List<Map<String, String>>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách màu sắc thành công",
                    colors
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/{productId}/sizes - Lấy danh sách kích thước của sản phẩm
     */
    @GetMapping("/{productId}/sizes")
    public ResponseEntity<?> getProductSizes(@PathVariable int productId) {
        try {
            List<String> sizes = productInfoService.getDistinctSizes(productId);
            
            ApiResponse<List<String>> response = new ApiResponse<>(
                    200,
                    "Lấy danh sách kích thước thành công",
                    sizes
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * GET /api/products/info/{id} - Lấy product info theo ID
     */
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getProductInfoById(@PathVariable int id) {
        try {
            ProductInfo productInfo = productInfoService.findById(id);
            
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    200,
                    "Lấy thông tin biến thể thành công",
                    productInfo
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    404,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    /**
     * POST /api/products/{productId}/info - Tạo product info mới
     */
    @PostMapping("/{productId}/info")
    public ResponseEntity<?> createProductInfo(
            @PathVariable int productId,
            @RequestBody ProductInfo productInfo) {
        try {
            ProductInfo created = productInfoService.create(productId, productInfo);
            
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    201,
                    "Tạo biến thể sản phẩm thành công",
                    created
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * POST /api/products/{productId}/info/batch - Tạo nhiều product info cùng lúc
     */
    @PostMapping("/{productId}/info/batch")
    public ResponseEntity<?> createMultipleProductInfo(
            @PathVariable int productId,
            @RequestBody List<ProductInfo> productInfoList) {
        try {
            List<ProductInfo> created = productInfoService.saveAll(productId, productInfoList);
            
            ApiResponse<List<ProductInfo>> response = new ApiResponse<>(
                    201,
                    "Tạo biến thể sản phẩm thành công",
                    created
            );
            
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * PUT /api/products/info/{id} - Cập nhật product info
     */
    @PutMapping("/info/{id}")
    public ResponseEntity<?> updateProductInfo(
            @PathVariable int id,
            @RequestBody ProductInfo productInfo) {
        try {
            ProductInfo updated = productInfoService.update(id, productInfo);
            
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    200,
                    "Cập nhật biến thể sản phẩm thành công",
                    updated
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * PATCH /api/products/info/{id}/quantity - Cập nhật số lượng
     */
    @PatchMapping("/info/{id}/quantity")
    public ResponseEntity<?> updateQuantity(
            @PathVariable int id,
            @RequestParam int quantity) {
        try {
            ProductInfo updated = productInfoService.updateQuantity(id, quantity);
            
            ApiResponse<ProductInfo> response = new ApiResponse<>(
                    200,
                    "Cập nhật số lượng thành công",
                    updated
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * DELETE /api/products/info/{id} - Xóa product info
     */
    @DeleteMapping("/info/{id}")
    public ResponseEntity<?> deleteProductInfo(@PathVariable int id) {
        try {
            productInfoService.delete(id);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    200,
                    "Xóa biến thể sản phẩm thành công",
                    null
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * DELETE /api/products/{productId}/info - Xóa tất cả product info của sản phẩm
     */
    @DeleteMapping("/{productId}/info")
    public ResponseEntity<?> deleteAllProductInfo(@PathVariable int productId) {
        try {
            productInfoService.deleteByProductId(productId);
            
            ApiResponse<Void> response = new ApiResponse<>(
                    200,
                    "Xóa tất cả biến thể sản phẩm thành công",
                    null
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    400,
                    e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
