package iuh.fit.xstore.service;

import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductSales;
import iuh.fit.xstore.repository.ProductRepository;
import iuh.fit.xstore.repository.ProductSalesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSalesService {

    private final ProductSalesRepository productSalesRepository;
    private final ProductRepository productRepository;

    public List<ProductSales> getAllProductSales() {
        return productSalesRepository.findAll();
    }

    public Optional<ProductSales> getProductSalesByProductId(int productId) {
        return productSalesRepository.findByProductId(productId);
    }

    @Transactional
    public ProductSales createProductSales(ProductSales productSales) {
        // Set original price from product
        Product product = productSales.getProduct();
        productSales.setOriginalPrice(product.getPrice());

        // Calculate discounted price
        double discountPercent = productSales.getDiscountPercent();
        double discountedPrice = product.getPrice() * (100 - discountPercent) / 100;
        productSales.setDiscountedPrice(discountedPrice);

        // Save ProductSales
        ProductSales saved = productSalesRepository.save(productSales);

        // Update Product.isSale
        product.setIsSale(true);
        productRepository.save(product);

        return saved;
    }

    @Transactional
    public void deleteProductSales(int productId) {
        Optional<ProductSales> productSalesOpt = productSalesRepository.findByProductId(productId);
        if (productSalesOpt.isPresent()) {
            productSalesRepository.delete(productSalesOpt.get());

            // Update Product.isSale
            Product product = productSalesOpt.get().getProduct();
            product.setIsSale(false);
            productRepository.save(product);
        }
    }

    @Transactional
    public ProductSales updateProductSales(int productId, ProductSales updatedProductSales) {
        Optional<ProductSales> existingOpt = productSalesRepository.findByProductId(productId);
        if (existingOpt.isPresent()) {
            ProductSales existing = existingOpt.get();

            // Update fields
            existing.setDiscountPercent(updatedProductSales.getDiscountPercent());
            existing.setStartDate(updatedProductSales.getStartDate());
            existing.setEndDate(updatedProductSales.getEndDate());

            // Recalculate discounted price
            double discountPercent = updatedProductSales.getDiscountPercent();
            double discountedPrice = existing.getProduct().getPrice() * (100 - discountPercent) / 100;
            existing.setDiscountedPrice(discountedPrice);

            return productSalesRepository.save(existing);
        }
        return null;
    }
}
