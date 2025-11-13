package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.ShipInfo;
import iuh.fit.xstore.model.User;
import iuh.fit.xstore.repository.ShipInfoRepository;
import iuh.fit.xstore.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class ShipInfoService {
    private final ShipInfoRepository shipInfoRepo;
    private final UserRepository userRepo;

    /**
     * Lấy tất cả ship info
     */
    public List<ShipInfo> findAll() {
        return shipInfoRepo.findAll();
    }

    /**
     * Lấy ship info theo ID
     */
    public ShipInfo findById(int id) {
        return shipInfoRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SHIPINFO_NOT_FOUND));
    }

    /**
     * Lấy tất cả ship info của một user
     */
    public List<ShipInfo> findByUserId(int userId) {
        log.info("📦 Fetching ship infos for user: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        return shipInfoRepo.findByUserId(userId);
    }

    /**
     * Lấy ship info mặc định của user
     */
    public ShipInfo findDefaultByUserId(int userId) {
        log.info("📦 Fetching default ship info for user: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        return shipInfoRepo.findDefaultByUserId(userId)
                .orElse(null);
    }

    /**
     * Tạo ship info mới cho user
     */
    public ShipInfo createShipInfoForUser(ShipInfo shipInfo, int userId) {
        log.info("📦 Creating ship info for user: {}", userId);
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        shipInfo.setUser(user);
        
        // Nếu là mặc định, reset các ship info cũ
        if (shipInfo.isDefault()) {
            List<ShipInfo> userShipInfos = shipInfoRepo.findByUserId(userId);
            for (ShipInfo si : userShipInfos) {
                si.setDefault(false);
                shipInfoRepo.save(si);
            }
        }

        ShipInfo savedShipInfo = shipInfoRepo.save(shipInfo);
        log.info("✅ Ship info created: {}", savedShipInfo.getId());
        
        return savedShipInfo;
    }

    /**
     * Tạo ship info mới (chung)
     */
    public ShipInfo createShipInfo(ShipInfo shipInfo) {
        return shipInfoRepo.save(shipInfo);
    }

    /**
     * Cập nhật ship info
     */
    public ShipInfo updateShipInfo(ShipInfo shipInfo) {
        ShipInfo existedShipInfo = findById(shipInfo.getId());

        if (shipInfo.getRecipientName() != null) {
            existedShipInfo.setRecipientName(shipInfo.getRecipientName());
        }
        if (shipInfo.getRecipientPhone() != null) {
            existedShipInfo.setRecipientPhone(shipInfo.getRecipientPhone());
        }
        if (shipInfo.getStreetNumber() != null) {
            existedShipInfo.setStreetNumber(shipInfo.getStreetNumber());
        }
        if (shipInfo.getStreetName() != null) {
            existedShipInfo.setStreetName(shipInfo.getStreetName());
        }
        if (shipInfo.getWard() != null) {
            existedShipInfo.setWard(shipInfo.getWard());
        }
        if (shipInfo.getDistrict() != null) {
            existedShipInfo.setDistrict(shipInfo.getDistrict());
        }
        if (shipInfo.getCity() != null) {
            existedShipInfo.setCity(shipInfo.getCity());
        }

        return shipInfoRepo.save(existedShipInfo);
    }

    /**
     * Xóa ship info
     */
    public int deleteShipInfo(int id) {
        findById(id);
        shipInfoRepo.deleteById(id);
        log.info("✅ Ship info deleted: {}", id);
        return id;
    }

    /**
     * Đặt ship info làm mặc định cho user
     */
    public ShipInfo setDefaultShipInfo(int shipInfoId, int userId) {
        log.info("📦 Setting default ship info {} for user {}", shipInfoId, userId);
        
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ShipInfo shipInfo = findById(shipInfoId);

        // Reset tất cả ship info của user
        List<ShipInfo> userShipInfos = shipInfoRepo.findByUserId(userId);
        for (ShipInfo si : userShipInfos) {
            si.setDefault(false);
            shipInfoRepo.save(si);
        }

        // Set ship info này là mặc định
        shipInfo.setDefault(true);
        ShipInfo saved = shipInfoRepo.save(shipInfo);
        
        log.info("✅ Default ship info set: {}", shipInfoId);
        return saved;
    }
}
