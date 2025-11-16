package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ProductSalesRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductSales;
import iuh.fit.xstore.service.ProductSalesService;
import iuh.fit.xstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-sales")
@RequiredArgsConstructor
public class ProductSalesController {

    private final ProductSalesService productSalesService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductSales>>> getAllProductSales() {
        List<ProductSales> productSales = productSalesService.getAllProductSales();
        return ResponseEntity.ok(new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productSales));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductSales>> getProductSalesByProductId(@PathVariable int productId) {
        Optional<ProductSales> productSales = productSalesService.getProductSalesByProductId(productId);
        if (productSales.isPresent()) {
            return ResponseEntity.ok(new ApiResponse<>(SuccessCode.FETCH_SUCCESS, productSales.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductSales>> createProductSales(@RequestBody ProductSalesRequest request) {
        Product product = productService.findById(request.getProductId());

        ProductSales productSales = ProductSales.builder()
                .product(product)
                .discountPercent(request.getDiscountPercent())
                .discountedPrice(request.getDiscountedPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        ProductSales created = productSalesService.createProductSales(productSales);
        return ResponseEntity.ok(new ApiResponse<>(SuccessCode.PRODUCT_SALES_CREATED, created));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductSales>> updateProductSales(
            @PathVariable int productId,
            @RequestBody ProductSalesRequest request) {

        ProductSales updatedData = ProductSales.builder()
                .discountPercent(request.getDiscountPercent())
                .discountedPrice(request.getDiscountedPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        ProductSales updated = productSalesService.updateProductSales(productId, updatedData);
        if (updated != null) {
            return ResponseEntity.ok(new ApiResponse<>(SuccessCode.PRODUCT_SALES_UPDATED, updated));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductSales(@PathVariable int productId) {
        productSalesService.deleteProductSales(productId);
        return ResponseEntity.ok(new ApiResponse<>(SuccessCode.PRODUCT_SALES_DELETED, null));
    }
}