package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.dto.response.SuccessCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class UploadController {
    private final String UPLOAD_DIR = "uploads";
    private final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );
    private final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
        "video/mp4", "video/avi", "video/mov", "video/wmv", "video/flv", "video/webm"
    );

    @PostMapping("/upload")
    public ApiResponse<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam(value = "type", defaultValue = "comment") String type) {
        if (file.isEmpty()) {
            return new ApiResponse<>(ErrorCode.FILE_EMPTY);
        }

        // Kiểm tra kích thước file
        if (file.getSize() > MAX_FILE_SIZE) {
            return new ApiResponse<>(400, "File quá lớn. Kích thước tối đa là 10MB", null);
        }

        // Kiểm tra loại file
        String contentType = file.getContentType();
        if (!ALLOWED_IMAGE_TYPES.contains(contentType) && !ALLOWED_VIDEO_TYPES.contains(contentType)) {
            return new ApiResponse<>(400, "Loại file không được hỗ trợ. Chỉ chấp nhận ảnh và video", null);
        }

        try  {
            // Xác định thư mục dựa trên type
            String subDir = "comments";
            if ("avatar".equals(type)) {
                subDir = "avatars";
            } else if ("product".equals(type)) {
                subDir = "products";
            }

            // Tạo thư mục nếu chưa có
            File dir = new File(UPLOAD_DIR + "/" + subDir);
            if (!dir.exists()) dir.mkdirs();

            // Tạo tên file duy nhất
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Tạo đường dẫn file
            Path filePath = Paths.get(UPLOAD_DIR + "/" + subDir, uniqueFilename);

            // Lưu file
            Files.write(filePath, file.getBytes());

            // Trả về đường dẫn tương đối
            String fileUrl = "/" + subDir + "/" + uniqueFilename;
            return new ApiResponse<>(SuccessCode.FILE_UPLAOD_SUCCESSFULLY, fileUrl);

        } catch (Exception e) {
            return new ApiResponse<>(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
