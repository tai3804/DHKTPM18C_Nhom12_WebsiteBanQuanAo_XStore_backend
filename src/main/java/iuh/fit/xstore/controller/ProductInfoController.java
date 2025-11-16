package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.service.ProductInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;
import java.util.UUID;

/**
 * ProductInfoController - REST API cho ProductInfo
 */
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductInfoController {
    
    private final ProductInfoService productInfoService;

    // 📁 Thư mục lưu ảnh biến thể
    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * ✅ Lưu file ảnh biến thể lên server
     */
    private String saveProductInfoImage(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Tạo thư mục nếu chưa tồn tại
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Lấy tên file gốc và xóa spaces, thay bằng underscore
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            originalFilename = originalFilename.replaceAll("\\s+", "_");  // Xóa tất cả spaces
        } else {
            originalFilename = "image";
        }

        // Tạo tên file unique: productInfoImage_UUID_cleanedname
        String fileName = "productInfoImage_" + UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file
        Files.write(filePath, file.getBytes());

        // Return đường dẫn tương đối để frontend có thể access
        return "/uploads/products/" + fileName;
    }

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
     * POST /api/products/{productId}/info/upload - Tạo product info với ảnh (multipart/form-data)
     */
    @PostMapping("/{productId}/info/upload")
    public ResponseEntity<?> createProductInfoWithImage(
            @PathVariable int productId,
            @RequestParam String colorName,
            @RequestParam String colorHexCode,
            @RequestParam String sizeName,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            // 💾 Xử lý ảnh nếu có
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = saveProductInfoImage(image);
                System.out.println("✅ Variant image saved: " + imagePath);
            }

            // Tạo ProductInfo object
            ProductInfo productInfo = new ProductInfo();
            productInfo.setColorName(colorName);
            productInfo.setColorHexCode(colorHexCode);
            productInfo.setSizeName(sizeName);
            productInfo.setImage(imagePath);

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
     * POST /api/products/{productId}/info/batch/upload - Tạo nhiều product info với ảnh cùng lúc (multipart/form-data)
     */
    @PostMapping("/{productId}/info/batch/upload")
    public ResponseEntity<?> createMultipleProductInfoWithImages(
            @PathVariable int productId,
            @RequestParam List<String> colorNames,
            @RequestParam List<String> colorHexCodes,
            @RequestParam List<String> sizeNames,
            @RequestParam(required = false) List<MultipartFile> images
    ) {
        try {
            List<ProductInfo> productInfoList = new ArrayList<>();
            
            for (int i = 0; i < colorNames.size(); i++) {
                ProductInfo info = new ProductInfo();
                info.setColorName(colorNames.get(i));
                info.setColorHexCode(colorHexCodes.get(i));
                info.setSizeName(sizeNames.get(i));
                
                // Xử lý ảnh nếu có
                if (images != null && i < images.size() && images.get(i) != null && !images.get(i).isEmpty()) {
                    String imagePath = saveProductInfoImage(images.get(i));
                    info.setImage(imagePath);
                }
                
                productInfoList.add(info);
            }

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
     * PUT /api/products/info/{id}/upload - Cập nhật product info với ảnh (multipart/form-data)
     */
    @PutMapping("/info/{id}/upload")
    public ResponseEntity<?> updateProductInfoWithImage(
            @PathVariable int id,
            @RequestParam String colorName,
            @RequestParam String colorHexCode,
            @RequestParam String sizeName,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            // 💾 Xử lý ảnh: nếu có file mới, lưu file mới; nếu không, giữ ảnh cũ
            String imagePath = null;
            if (image != null && !image.isEmpty()) {
                imagePath = saveProductInfoImage(image);
                System.out.println("✅ New variant image saved: " + imagePath);
            } else {
                // Giữ ảnh cũ: fetch product info cũ từ DB
                ProductInfo existingInfo = productInfoService.findById(id);
                imagePath = existingInfo.getImage();
                System.out.println("ℹ️ Keeping old variant image: " + imagePath);
            }

            // Tạo ProductInfo object với dữ liệu mới
            ProductInfo productInfo = new ProductInfo();
            productInfo.setColorName(colorName);
            productInfo.setColorHexCode(colorHexCode);
            productInfo.setSizeName(sizeName);
            productInfo.setImage(imagePath);

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
