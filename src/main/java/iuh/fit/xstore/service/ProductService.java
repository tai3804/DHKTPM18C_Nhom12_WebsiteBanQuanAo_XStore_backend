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
import org.springframework.transaction.annotation.Transactional;

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
    
    @Transactional
    public Product createProduct(Product product) {
        // ✅ Khi tạo mới, ID sẽ được tự động generate, không cần check existsById
        System.out.println("🔧 Creating product: " + product.getName());
        System.out.println("   Colors: " + (product.getColors() != null ? product.getColors().size() : 0));
        if (product.getColors() != null) {
            product.getColors().forEach(c -> System.out.println("   - Color: " + c.getName() + " (#" + c.getHexCode() + ")"));
        }
        System.out.println("   Sizes: " + (product.getSizes() != null ? product.getSizes().size() : 0));
        if (product.getSizes() != null) {
            product.getSizes().forEach(s -> System.out.println("   - Size: " + s.getName()));
        }
        
        // Lưu product trước (ID sẽ được generate)
        Product savedProduct = productRepository.save(product);
        System.out.println("✅ Product saved with ID: " + savedProduct.getId());
        
        // ✅ Lưu colors
        if (product.getColors() != null && !product.getColors().isEmpty()) {
            System.out.println("🎨 Saving " + product.getColors().size() + " colors...");
            for (ProductColor color : product.getColors()) {
                // Không set ID - let Hibernate generate new ID
                color.setProduct(savedProduct);
                ProductColor saved = productColorRepository.save(color);
                System.out.println("   ✅ Color saved: " + saved.getName() + " with ID: " + saved.getId());
            }
        }
        
        // ✅ Lưu sizes
        if (product.getSizes() != null && !product.getSizes().isEmpty()) {
            System.out.println("📏 Saving " + product.getSizes().size() + " sizes...");
            for (ProductSize size : product.getSizes()) {
                // Không set ID - let Hibernate generate new ID
                size.setProduct(savedProduct);
                ProductSize saved = productSizeRepository.save(size);
                System.out.println("   ✅ Size saved: " + saved.getName() + " with ID: " + saved.getId());
            }
        }
        
        // ✅ Flush để chắc chắn dữ liệu được write vào DB
        productColorRepository.flush();
        productSizeRepository.flush();
        
        // Reload product để lấy colors và sizes từ database
        Product reloaded = productRepository.findById(savedProduct.getId()).orElse(savedProduct);
        
        // Force initialize colors và sizes (vì là LAZY load)
        if (reloaded.getColors() != null) {
            reloaded.getColors().size();
        }
        if (reloaded.getSizes() != null) {
            reloaded.getSizes().size();
        }
        
        System.out.println("✅ Product reloaded with " + 
            (reloaded.getColors() != null ? reloaded.getColors().size() : 0) + 
            " colors and " + 
            (reloaded.getSizes() != null ? reloaded.getSizes().size() : 0) + 
            " sizes");
        return reloaded;
    }
    
    @Transactional
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
        
        // ✅ Cập nhật colors
        if (product.getColors() != null && !product.getColors().isEmpty()) {
            // Xóa colors cũ
            productColorRepository.deleteByProduct_Id(existingProduct.getId());
            productColorRepository.flush();
            
            // Thêm colors mới
            for (ProductColor color : product.getColors()) {
                color.setProduct(existingProduct);
                productColorRepository.save(color);
            }
            productColorRepository.flush();
            existingProduct.setColors(product.getColors());
        }
        
        // ✅ Cập nhật sizes
        if (product.getSizes() != null && !product.getSizes().isEmpty()) {
            // Xóa sizes cũ
            productSizeRepository.deleteByProduct_Id(existingProduct.getId());
            productSizeRepository.flush();
            
            // Thêm sizes mới
            for (ProductSize size : product.getSizes()) {
                size.setProduct(existingProduct);
                productSizeRepository.save(size);
            }
            productSizeRepository.flush();
            existingProduct.setSizes(product.getSizes());
        }
        
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
        return productRepository.searchProducts(keyword.trim().toLowerCase());
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
     * ✅ Xoá một color theo ID
     */
    public void deleteProductColor(int colorId) {
        System.out.println("🗑️ Service: Deleting color ID " + colorId);
        productColorRepository.deleteById(colorId);
    }

    /**
     * ✅ Xoá một size theo ID
     */
    public void deleteProductSize(int sizeId) {
        System.out.println("🗑️ Service: Deleting size ID " + sizeId);
        productSizeRepository.deleteById(sizeId);
    }

    /**
     * ✅ Xoá tất cả colors của một product
     */
    public void deleteAllProductColors(int productId) {
        System.out.println("🗑️ Service: Deleting all colors for product ID " + productId);
        productColorRepository.deleteByProduct_Id(productId);
    }

    /**
     * ✅ Xoá tất cả sizes của một product
     */
    public void deleteAllProductSizes(int productId) {
        System.out.println("🗑️ Service: Deleting all sizes for product ID " + productId);
        productSizeRepository.deleteByProduct_Id(productId);
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