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

@RestController
@RequestMapping("/file")
public class UploadController {
    private final String UPLOAD_DIR = "uploads";

    @PostMapping("/upload")
    public ApiResponse<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ApiResponse<>(ErrorCode.FILE_EMPTY);
        }

        try  {
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // Tạo đường dẫn file
            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());

            // Lưu file
            Files.write(filePath, file.getBytes());

            System.out.println(file);
            return new ApiResponse<>(SuccessCode.FILE_UPLAOD_SUCCESSFULLY, file.getName());

        } catch (Exception e) {
            return new ApiResponse<>(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
