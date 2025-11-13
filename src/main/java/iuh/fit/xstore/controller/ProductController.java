package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.request.ProductCreateRequest;
import iuh.fit.xstore.dto.request.ProductColorDTO;
import iuh.fit.xstore.dto.request.ProductSizeDTO;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductColor;
import iuh.fit.xstore.model.ProductSize;
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
            @RequestParam String colors,      // JSON string: [{"name":"Red","hexCode":"#FF0000"},...]
            @RequestParam String sizes,       // JSON string: [{"name":"S","description":"Small"},...]
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
        product.setImage(imagePath);  // Lưu đường dẫn ảnh
        product.setBrand(brand);
        product.setFabric(fabric);
        product.setPrice(price);
        product.setPriceInStock(priceInStock);

        // ✅ Lấy ProductType từ ID
        if (typeId > 0) {
            ProductType type = productTypeService.findById(typeId);
            product.setType(type);
        }

        // ✅ Parse colors JSON string thành ProductColor entities
        List<ProductColor> colorList = new ArrayList<>();
        try {
            List<ProductColorDTO> colorDtos = objectMapper.readValue(
                    colors,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductColorDTO.class)
            );
            for (ProductColorDTO colorDto : colorDtos) {
                ProductColor color = new ProductColor();
                color.setName(colorDto.getName());
                color.setHexCode(colorDto.getHexCode());
                colorList.add(color);
            }
        } catch (Exception e) {
            System.err.println("❌ Error parsing colors: " + e.getMessage());
            throw new RuntimeException("Invalid colors format");
        }
        product.setColors(colorList);

        // ✅ Parse sizes JSON string thành ProductSize entities
        List<ProductSize> sizeList = new ArrayList<>();
        try {
            List<ProductSizeDTO> sizeDtos = objectMapper.readValue(
                    sizes,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductSizeDTO.class)
            );
            for (ProductSizeDTO sizeDto : sizeDtos) {
                ProductSize size = new ProductSize();
                size.setName(sizeDto.getName());
                size.setDescription(sizeDto.getDescription());
                sizeList.add(size);
            }
        } catch (Exception e) {
            System.err.println("❌ Error parsing sizes: " + e.getMessage());
            throw new RuntimeException("Invalid sizes format");
        }
        product.setSizes(sizeList);

        System.out.println("✅ Creating product with " + colorList.size() + " colors and " + sizeList.size() + " sizes");
        Product createdProduct = productService.createProduct(product);
        System.out.println("✅ Product created successfully: ID " + createdProduct.getId());
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    /**
     * ✅ Cập nhật sản phẩm với file ảnh mới (multipart/form-data)
     * PUT /api/products/{id}/upload
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
            @RequestParam String colors,      // JSON string
            @RequestParam String sizes,       // JSON string
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

        // ✅ Parse colors JSON string thành ProductColor entities
        List<ProductColor> colorList = new ArrayList<>();
        try {
            List<ProductColorDTO> colorDtos = objectMapper.readValue(
                    colors,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductColorDTO.class)
            );
            for (ProductColorDTO colorDto : colorDtos) {
                ProductColor color = new ProductColor();
                color.setName(colorDto.getName());
                color.setHexCode(colorDto.getHexCode());
                colorList.add(color);
            }
        } catch (Exception e) {
            System.err.println("❌ Error parsing colors: " + e.getMessage());
            throw new RuntimeException("Invalid colors format");
        }
        product.setColors(colorList);

        // ✅ Parse sizes JSON string thành ProductSize entities
        List<ProductSize> sizeList = new ArrayList<>();
        try {
            List<ProductSizeDTO> sizeDtos = objectMapper.readValue(
                    sizes,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductSizeDTO.class)
            );
            for (ProductSizeDTO sizeDto : sizeDtos) {
                ProductSize size = new ProductSize();
                size.setName(sizeDto.getName());
                size.setDescription(sizeDto.getDescription());
                sizeList.add(size);
            }
        } catch (Exception e) {
            System.err.println("❌ Error parsing sizes: " + e.getMessage());
            throw new RuntimeException("Invalid sizes format");
        }
        product.setSizes(sizeList);

        System.out.println("✅ Updating product with " + colorList.size() + " colors and " + sizeList.size() + " sizes");
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
        System.out.println("📥 Received product data:");
        System.out.println("   Name: " + request.getName());
        System.out.println("   Colors: " + request.getColors().size());
        System.out.println("   Sizes: " + request.getSizes().size());
        
        // ✅ Chuyển DTO thành Product entity
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImage(request.getImage());
        product.setBrand(request.getBrand());
        product.setFabric(request.getFabric());
        product.setPrice(request.getPrice());
        product.setPriceInStock(request.getPriceInStock());
        
        // ✅ Lấy ProductType từ ID
        if (request.getTypeId() > 0) {
            ProductType type = productTypeService.findById(request.getTypeId());
            product.setType(type);
        }
        
        // ✅ Chuyển colors DTO thành ProductColor entity
        List<ProductColor> colors = new ArrayList<>();
        for (ProductColorDTO colorDto : request.getColors()) {
            ProductColor color = new ProductColor();
            color.setName(colorDto.getName());
            color.setHexCode(colorDto.getHexCode());
            colors.add(color);
        }
        product.setColors(colors);
        
        // ✅ Chuyển sizes DTO thành ProductSize entity
        List<ProductSize> sizes = new ArrayList<>();
        for (ProductSizeDTO sizeDto : request.getSizes()) {
            ProductSize size = new ProductSize();
            size.setName(sizeDto.getName());
            size.setDescription(sizeDto.getDescription());
            sizes.add(size);
        }
        product.setSizes(sizes);
        
        Product createdProduct = productService.createProduct(product);
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable int id, @RequestBody ProductCreateRequest request) {
        System.out.println("📥 Updating product ID: " + id);
        System.out.println("   Request: " + request);
        System.out.println("   Name: " + request.getName());
        System.out.println("   TypeId: " + request.getTypeId());
        System.out.println("   Colors: " + (request.getColors() != null ? request.getColors().size() : 0));
        System.out.println("   Sizes: " + (request.getSizes() != null ? request.getSizes().size() : 0));
        
        try {
            // ✅ Chuyển DTO thành Product entity
            Product product = new Product();
            product.setId(id);
            product.setName(request.getName());
            product.setDescription(request.getDescription());
            product.setImage(request.getImage());
            product.setBrand(request.getBrand());
            product.setFabric(request.getFabric());
            product.setPrice(request.getPrice());
            product.setPriceInStock(request.getPriceInStock());
            
            // ✅ Lấy ProductType từ ID
            if (request.getTypeId() > 0) {
                ProductType type = productTypeService.findById(request.getTypeId());
                product.setType(type);
            }
            
            // ✅ Chuyển colors DTO thành ProductColor entity
            List<ProductColor> colors = new ArrayList<>();
            if (request.getColors() != null) {
                for (ProductColorDTO colorDto : request.getColors()) {
                    ProductColor color = new ProductColor();
                    color.setName(colorDto.getName());
                    color.setHexCode(colorDto.getHexCode());
                    colors.add(color);
                }
            }
            product.setColors(colors);
            
            // ✅ Chuyển sizes DTO thành ProductSize entity
            List<ProductSize> sizes = new ArrayList<>();
            if (request.getSizes() != null) {
                for (ProductSizeDTO sizeDto : request.getSizes()) {
                    ProductSize size = new ProductSize();
                    size.setName(sizeDto.getName());
                    size.setDescription(sizeDto.getDescription());
                    sizes.add(size);
                }
            }
            product.setSizes(sizes);
            
            System.out.println("✅ Product entity prepared: " + product.getName());
            
            Product updatedProduct = productService.updateProduct(product);
            System.out.println("✅ Product updated successfully");
            return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
        } catch (Exception e) {
            System.err.println("❌ Error updating product: " + e.getMessage());
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

    @GetMapping("/{id}/colors")
    public ApiResponse<List<ProductColor>> getProductColors(@PathVariable int id) {
        List<ProductColor> colors = productService.getProductColors(id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, colors);
    }

    @GetMapping("/{id}/sizes")
    public ApiResponse<List<ProductSize>> getProductSizes(@PathVariable int id) {
        List<ProductSize> sizes = productService.getProductSizes(id);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, sizes);
    }

    /**
     * ✅ Xoá một color của sản phẩm
     * DELETE /api/products/{productId}/colors/{colorId}
     */
    @DeleteMapping("/{productId}/colors/{colorId}")
    public ApiResponse<String> deleteProductColor(@PathVariable int productId, @PathVariable int colorId) {
        System.out.println("🗑️ Deleting color " + colorId + " from product " + productId);
        productService.deleteProductColor(colorId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, "Color deleted successfully");
    }

    /**
     * ✅ Xoá một size của sản phẩm
     * DELETE /api/products/{productId}/sizes/{sizeId}
     */
    @DeleteMapping("/{productId}/sizes/{sizeId}")
    public ApiResponse<String> deleteProductSize(@PathVariable int productId, @PathVariable int sizeId) {
        System.out.println("🗑️ Deleting size " + sizeId + " from product " + productId);
        productService.deleteProductSize(sizeId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, "Size deleted successfully");
    }

    /**
     * ✅ Xoá tất cả colors của sản phẩm
     * DELETE /api/products/{productId}/colors
     */
    @DeleteMapping("/{productId}/colors")
    public ApiResponse<String> deleteAllProductColors(@PathVariable int productId) {
        System.out.println("🗑️ Deleting all colors from product " + productId);
        productService.deleteAllProductColors(productId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, "All colors deleted successfully");
    }

    /**
     * ✅ Xoá tất cả sizes của sản phẩm
     * DELETE /api/products/{productId}/sizes
     */
    @DeleteMapping("/{productId}/sizes")
    public ApiResponse<String> deleteAllProductSizes(@PathVariable int productId) {
        System.out.println("🗑️ Deleting all sizes from product " + productId);
        productService.deleteAllProductSizes(productId);
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, "All sizes deleted successfully");
    }

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
