package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.request.ProductCreateRequest;
import iuh.fit.xstore.dto.request.ProductUpdateRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.model.ProductType;
import iuh.fit.xstore.service.ProductService;
import iuh.fit.xstore.service.ProductTypeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 📁 Thư mục lưu ảnh
    private static final String UPLOAD_DIR = "uploads/products/";

    /**
     * ✅ Lưu file ảnh lên server
     */
    private String saveProductImage(MultipartFile file) throws Exception {
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

        // Tạo tên file unique: productImage_UUID_cleanedname
        String fileName = "productImage_" + UUID.randomUUID() + "_" + originalFilename;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // Lưu file
        Files.write(filePath, file.getBytes());

        // Return đường dẫn tương đối để frontend có thể access
        return "/uploads/products/" + fileName;
    }

    /**
     * ✅ Tạo sản phẩm mới với file ảnh (multipart/form-data)
     * POST /api/products/upload
     * NOTE: ProductInfo (colors, sizes, quantities) sẽ được quản lý riêng qua ProductInfoController
     */
    @PostMapping("/upload")
    public ApiResponse<Product> createProductWithImage(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String brand,
            @RequestParam String fabric,
            @RequestParam Double price,
            @RequestParam Double priceInStock,
            @RequestParam int typeId,
            @RequestParam(required = false) MultipartFile image
    ) throws Exception {
        System.out.println("📥 [CREATE MULTIPART] Received product data:");
        System.out.println("   Name: " + name);
        System.out.println("   Brand: " + brand);
        System.out.println("   Price: " + price);
        System.out.println("   TypeId: " + typeId);
        System.out.println("   Image file: " + (image != null ? image.getOriginalFilename() : "null"));

        // 💾 Lưu ảnh nếu có
        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = saveProductImage(image);
            System.out.println("✅ Image saved: " + imagePath);
        }

        // ✅ Chuyển DTO thành Product entity
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setImage(imagePath);
        product.setBrand(brand);
        product.setFabric(fabric);
        product.setPrice(price);
        product.setPriceInStock(priceInStock);

        // ✅ Lấy ProductType từ ID
        if (typeId > 0) {
            ProductType type = productTypeService.findById(typeId);
            product.setType(type);
        }

        // ProductInfo (colors, sizes, quantities) sẽ được thêm sau qua ProductInfoController
        Product createdProduct = productService.createProduct(product);
        System.out.println("✅ Product created successfully: ID " + createdProduct.getId());
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    /**
     * ✅ Cập nhật sản phẩm với file ảnh mới (multipart/form-data)
     * PUT /api/products/{id}/upload
     * NOTE: ProductInfo (colors, sizes, quantities) sẽ được quản lý riêng qua ProductInfoController
     */
    @PutMapping("/{id}/upload")
    public ApiResponse<Product> updateProductWithImage(
            @PathVariable int id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String brand,
            @RequestParam String fabric,
            @RequestParam Double price,
            @RequestParam Double priceInStock,
            @RequestParam int typeId,
            @RequestParam(required = false) MultipartFile image
    ) throws Exception {
        System.out.println("📥 [UPDATE MULTIPART] Updating product ID: " + id);
        System.out.println("   Name: " + name);
        System.out.println("   Image file: " + (image != null ? image.getOriginalFilename() : "null"));

        // ✅ Chuyển DTO thành Product entity
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setBrand(brand);
        product.setFabric(fabric);
        product.setPrice(price);
        product.setPriceInStock(priceInStock);

        // 💾 Xử lý ảnh: nếu có file mới, lưu file mới; nếu không, giữ ảnh cũ
        if (image != null && !image.isEmpty()) {
            String newImagePath = saveProductImage(image);
            product.setImage(newImagePath);
            System.out.println("✅ New image saved: " + newImagePath);
        } else {
            // Giữ ảnh cũ: fetch product cũ từ DB
            Product existingProduct = productService.findById(id);
            product.setImage(existingProduct.getImage());
            System.out.println("ℹ️ Keeping old image: " + existingProduct.getImage());
        }

        // ✅ Lấy ProductType từ ID
        if (typeId > 0) {
            ProductType type = productTypeService.findById(typeId);
            product.setType(type);
        }

        // ProductInfo (colors, sizes, quantities) sẽ được cập nhật riêng qua ProductInfoController
        Product updatedProduct = productService.updateProduct(product);
        System.out.println("✅ Product updated successfully");
        return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
    }

    @GetMapping
    public ApiResponse<List<Product>> getAllProducts() {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productService.findAll());
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Backend is working! Products controller is accessible.";
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getProductById(@PathVariable int id) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productService.findById(id));
    }

    @GetMapping("/type/{typeId}")
    public ApiResponse<List<Product>> getProductsByTypeId(@PathVariable int typeId) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productService.findByTypeId(typeId));
    }

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setBrand(request.getBrand());
        product.setFabric(request.getFabric());
        product.setPrice(request.getPrice());
        product.setPriceInStock(request.getPriceInStock());

        if (request.getTypeId() > 0) {
            product.setType(productTypeService.findById(request.getTypeId()));
        }

        // --- Thêm phần ProductInfos ---
        if (request.getProductInfos() != null && !request.getProductInfos().isEmpty()) {
            List<ProductInfo> infos = request.getProductInfos().stream().map(infoReq -> {
                ProductInfo info = new ProductInfo();
                info.setColorName(infoReq.getColorName());
                info.setColorHexCode(infoReq.getColorHexCode());
                info.setSizeName(infoReq.getSizeName());
                info.setQuantity(infoReq.getQuantity());
                info.setImage(infoReq.getImage());
                info.setProduct(product); // quan trọng: gán product để cascade lưu
                return info;
            }).toList();
            product.setProductInfos(infos);
        }

        Product createdProduct = productService.createProduct(product);
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable int id, @RequestBody ProductUpdateRequest request) {

        try {
            Product product = new Product();
            product.setId(id);
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setImage(request.getImage());
            product.setBrand(request.getBrand());
            product.setFabric(request.getFabric());
            product.setPrice(request.getPrice());
            product.setPriceInStock(request.getPriceInStock());

            if (request.getTypeId() > 0) {
                ProductType type = productTypeService.findById(request.getTypeId());
                product.setType(type);
            }

            if (request.getProductInfos() != null && !request.getProductInfos().isEmpty()) {
                request.getProductInfos().forEach(info -> info.setProduct(product)); // quan trọng để cascade lưu
                product.setProductInfos(request.getProductInfos());
            }

            System.out.println("Product entity prepared: " + product.getName());

            Product updatedProduct = productService.updateProduct(product);
            System.out.println("Product updated successfully");
            return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return new ApiResponse<>(SuccessCode.PRODUCT_DELETED, id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Product>> searchProducts(@RequestParam(value = "q", required = false) String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, products);
    }

    @GetMapping("/{id}/stocks")
    public ApiResponse<List<Object>> getProductStocks(@PathVariable int id) {
        List<Object> stockItems = productService.getProductStocks(id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, stockItems);
    }

    // Colors và Sizes giờ được quản lý qua ProductInfoController
    // Xem ProductInfoController để biết thêm chi tiết

    /**
     * Filter products with advanced criteria
     * POST /api/products/filter
     * Body: ProductFilterRequest JSON
     */
    @PostMapping("/filter")
    public ApiResponse<List<Product>> filterProducts(@RequestBody ProductFilterRequest filterRequest) {
        List<Product> filteredProducts = productService.filterProducts(filterRequest);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, filteredProducts);
    }

    /**
     * Filter products with query parameters (alternative GET method)
     * GET /api/products/filter?productTypeId=1&minPrice=100000&maxPrice=500000&sortBy=price-asc
     */
    @GetMapping("/filter")
    public ApiResponse<List<Product>> filterProductsWithParams(
            @RequestParam(required = false) Integer productTypeId,
            @RequestParam(required = false) String productTypeName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sortBy
    ) {
        ProductFilterRequest filterRequest = new ProductFilterRequest();
        filterRequest.setProductTypeId(productTypeId);
        filterRequest.setProductTypeName(productTypeName);
        filterRequest.setMinPrice(minPrice);
        filterRequest.setMaxPrice(maxPrice);
        filterRequest.setSortBy(sortBy);

        List<Product> filteredProducts = productService.filterProducts(filterRequest);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, filteredProducts);
    }
}
