package iuh.fit.xstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCartRequest {
    private Integer userId; // ✅ Đổi từ Long sang Integer
}