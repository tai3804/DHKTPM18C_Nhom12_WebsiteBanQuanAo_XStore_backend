package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductColor;
import iuh.fit.xstore.model.ProductSize;
import iuh.fit.xstore.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

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
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ApiResponse<>(SuccessCode.PRODUCT_CREATED, createdProduct);
    }

    @PutMapping("/{id}")
    public ApiResponse<Product> updateProduct(@PathVariable int id, @RequestBody Product product) {
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);
        return new ApiResponse<>(SuccessCode.PRODUCT_UPDATED, updatedProduct);
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