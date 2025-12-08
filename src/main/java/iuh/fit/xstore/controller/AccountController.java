package iuh.fit.xstore.controller;

import iuh.fit.xstore.dto.request.ChangePasswordRequest;
import iuh.fit.xstore.dto.response.ApiResponse;
import iuh.fit.xstore.dto.response.SuccessCode;
import iuh.fit.xstore.model.Account;
import iuh.fit.xstore.model.Role;
import iuh.fit.xstore.security.UserDetail;
import iuh.fit.xstore.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Lấy tất cả tài khoản
    @GetMapping
    public ApiResponse<List<Account>> getAccounts() {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, accountService.findAll());
    }

    // Lấy tài khoản theo ID
    @GetMapping("/{id}")
    public ApiResponse<Account> getAccount(@PathVariable("id") int id) {
        return new ApiResponse<>(SuccessCode.FETCH_SUCCESS, accountService.findById(id));
    }

    // Tạo tài khoản mới
    @PostMapping
    public ApiResponse<Account> createAccount(@RequestBody Account account) {
        if (account.getRole() == null) {
            account.setRole(Role.CUSTOMER);
        }
        Account createdAccount = accountService.createAccount(account);
        return new ApiResponse<>(SuccessCode.ACCOUNT_CREATED, createdAccount);
    }

    // Cập nhật tài khoản
    @PutMapping("/{id}")
    public ApiResponse<Account> updateAccount(@PathVariable("id") int id, @RequestBody Account account) {
        account.setId(id);
        Account updatedAccount = accountService.updateAccount(account);
        return new ApiResponse<>(SuccessCode.ACCOUNT_UPDATED, updatedAccount);
    }

    // Xoá tài khoản theo ID
    @DeleteMapping("/{id}")
    public ApiResponse<Integer> deleteAccount(@PathVariable("id") int id) {
        accountService.deleteAccount(id);
        return new ApiResponse<>(SuccessCode.ACCOUNT_DELETED, id);
    }

    // Đổi mật khẩu
    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody ChangePasswordRequest request
    ) {
        accountService.changePassword(
                userDetail.getUsername(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        return new ApiResponse<>(SuccessCode.ACCOUNT_UPDATED, "Password changed successfully");
    }
}
