package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.Product;
import iuh.fit.xstore.model.ProductInfo;
import iuh.fit.xstore.repository.ProductInfoRepository;
import iuh.fit.xstore.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ProductInfoService - Service xử lý logic cho ProductInfo
 */
@Service
@AllArgsConstructor
public class ProductInfoService {
    
    private final ProductInfoRepository productInfoRepository;
    private final ProductRepository productRepository;

    /**
     * Lấy tất cả product info
     */
    public List<ProductInfo> findAll() {
        return productInfoRepository.findAll();
    }

    /**
     * Lấy product info theo ID
     */
    public ProductInfo findById(int id) {
        return productInfoRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_INFO_NOT_FOUND));
    }

    /**
     * Lấy tất cả product info của một sản phẩm
     */
    public List<ProductInfo> findByProductId(int productId) {
        // Kiểm tra product có tồn tại không
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        return productInfoRepository.findByProductId(productId);
    }

    /**
     * Lấy danh sách màu sắc unique của sản phẩm
     */
    public List<Map<String, String>> getDistinctColors(int productId) {
        List<Object[]> colors = productInfoRepository.findDistinctColorsByProductId(productId);
        return colors.stream()
                .map(color -> {
                    Map<String, String> colorMap = new HashMap<>();
                    colorMap.put("name", (String) color[0]);
                    colorMap.put("hexCode", (String) color[1]);
                    return colorMap;
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách size unique của sản phẩm
     */
    public List<String> getDistinctSizes(int productId) {
        return productInfoRepository.findDistinctSizesByProductId(productId);
    }

    /**
     * Tìm product info theo product, color và size
     */
    public ProductInfo findByProductColorSize(int productId, String colorName, String sizeName) {
        return productInfoRepository.findByProductIdAndColorAndSize(productId, colorName, sizeName)
                .orElse(null);
    }

    /**
     * Tạo product info mới
     */
    @Transactional
    public ProductInfo create(int productId, ProductInfo productInfo) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        productInfo.setProduct(product);
        
        return productInfoRepository.save(productInfo);
    }

    /**
     * Cập nhật product info
     */
    @Transactional
    public ProductInfo update(int id, ProductInfo productInfo) {
        ProductInfo existingInfo = findById(id);
        
        if (productInfo.getColorName() != null) {
            existingInfo.setColorName(productInfo.getColorName());
        }
        if (productInfo.getColorHexCode() != null) {
            existingInfo.setColorHexCode(productInfo.getColorHexCode());
        }
        if (productInfo.getSizeName() != null) {
            existingInfo.setSizeName(productInfo.getSizeName());
        }
        if (productInfo.getImage() != null) {
            existingInfo.setImage(productInfo.getImage());
        }
        
        return productInfoRepository.save(existingInfo);
    }

    /**
     * Xóa product info
     */
    @Transactional
    public void delete(int id) {
        ProductInfo productInfo = findById(id);
        productInfoRepository.delete(productInfo);
    }

    /**
     * Xóa tất cả product info của một sản phẩm
     */
    @Transactional
    public void deleteByProductId(int productId) {
        productInfoRepository.deleteByProductId(productId);
    }

    /**
     * Tạo hoặc cập nhật nhiều product info cùng lúc
     */
    @Transactional
    public List<ProductInfo> saveAll(int productId, List<ProductInfo> productInfoList) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        
        // Set product cho tất cả items
        productInfoList.forEach(info -> info.setProduct(product));
        
        return productInfoRepository.saveAll(productInfoList);
    }
}
