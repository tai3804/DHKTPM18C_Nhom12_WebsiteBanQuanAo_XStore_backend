package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.request.ProductFilterRequest;
import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductColor;
import iuh.fit.xstore.model.ProductSize;
import iuh.fit.xstore.model.ProductType;
import iuh.fit.xstore.model.StockItem;
import iuh.fit.xstore.repository.ProductRepository;
import iuh.fit.xstore.repository.ProductColorRepository;
import iuh.fit.xstore.repository.ProductSizeRepository;
import iuh.fit.xstore.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final StockItemRepository stockItemRepository;
    private final ProductColorRepository productColorRepository;
    private final ProductSizeRepository productSizeRepository;
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Product findById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
    
    public Product findByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
    
    public List<Product> findByType(ProductType type) {
        return productRepository.findByType(type);
    }
    
    public List<Product> findByTypeId(int typeId) {
        return productRepository.findByTypeId(typeId);
    }
    
    public List<Product> findByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }
    
    public Product createProduct(Product product) {
        if (productRepository.existsById(product.getId())) {
            throw new AppException(ErrorCode.PRODUCT_EXISTED);
        }
        return productRepository.save(product);
    }
    
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImage(product.getImage());
        existingProduct.setType(product.getType());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setFabric(product.getFabric());
        existingProduct.setPriceInStock(product.getPriceInStock());
        existingProduct.setPrice(product.getPrice());
        
        return productRepository.save(existingProduct);
    }
    
    public int deleteProduct(int id) {
        findById(id); // Check if product exists
        productRepository.deleteById(id);
        return id;
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.searchByName(keyword.trim());
    }

    public List<Object> getProductStocks(int productId) {
        // Verify product exists
        findById(productId);
        
        // Get all stock items for this product
        List<StockItem> stockItems = stockItemRepository.findByProduct_Id(productId);
        
        // Transform to simplified format for frontend
        return stockItems.stream()
                .map(item -> {
                    java.util.Map<String, Object> stockInfo = new java.util.HashMap<>();
                    stockInfo.put("stockId", item.getStock().getId());
                    stockInfo.put("stockName", item.getStock().getName());
                    stockInfo.put("quantity", item.getQuantity());
                    return stockInfo;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public List<ProductColor> getProductColors(int productId) {
        // Verify product exists
        findById(productId);
        
        // Get all colors for this product
        return productColorRepository.findByProduct_Id(productId);
    }

    public List<ProductSize> getProductSizes(int productId) {
        // Verify product exists
        findById(productId);
        
        // Get all sizes for this product
        return productSizeRepository.findByProduct_Id(productId);
    }

    /**
     * Filter products with advanced criteria
     * @param filterRequest Filter criteria
     * @return Filtered and sorted list of products
     */
    public List<Product> filterProducts(ProductFilterRequest filterRequest) {
        List<Product> products = productRepository.findAll();

        // Filter by product type ID
        if (filterRequest.getProductTypeId() != null) {
            products = products.stream()
                    .filter(p -> p.getType() != null && p.getType().getId() == filterRequest.getProductTypeId())
                    .collect(Collectors.toList());
        }

        // Filter by product type name
        if (filterRequest.getProductTypeName() != null && !filterRequest.getProductTypeName().trim().isEmpty()) {
            String typeName = filterRequest.getProductTypeName().trim();
            products = products.stream()
                    .filter(p -> p.getType() != null && p.getType().getName().equalsIgnoreCase(typeName))
                    .collect(Collectors.toList());
        }

        // Filter by minimum price
        if (filterRequest.getMinPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() >= filterRequest.getMinPrice())
                    .collect(Collectors.toList());
        }

        // Filter by maximum price
        if (filterRequest.getMaxPrice() != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() <= filterRequest.getMaxPrice())
                    .collect(Collectors.toList());
        }

        // Sort products
        if (filterRequest.getSortBy() != null && !filterRequest.getSortBy().isEmpty()) {
            switch (filterRequest.getSortBy().toLowerCase()) {
                case "price-asc":
                    products.sort(Comparator.comparingDouble(Product::getPrice));
                    break;
                case "price-desc":
                    products.sort(Comparator.comparingDouble(Product::getPrice).reversed());
                    break;
                case "name-asc":
                    products.sort(Comparator.comparing(Product::getName));
                    break;
                case "name-desc":
                    products.sort(Comparator.comparing(Product::getName).reversed());
                    break;
                case "newest":
                    products.sort(Comparator.comparingInt(Product::getId).reversed());
                    break;
                default:
                    // Keep default order
                    break;
            }
        }

        return products;
    }
}