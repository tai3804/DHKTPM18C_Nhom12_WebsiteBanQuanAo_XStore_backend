package iuh.fit.xstore.controller;

import iuh.fit.xstore.model.Account;
import iuh.fit.xstore.model.Role;
import iuh.fit.xstore.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // Lấy danh sách tất cả tài khoản
    @GetMapping
    public List<Account> getAccounts() {
        return accountService.findAll();
    }

    // Tạo tài khoản mới
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        if (account.getRole() == null) {
            account.setRole(Role.CUSTOMER);
        }
        return accountService.createAccount(account);
    }

    // Cập nhật tài khoản
    @PutMapping("/update")
    public Account updateAccount(@RequestBody Account account) {
        return accountService.updateAccount(account);
    }

    // Xoá tài khoản theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteAccount(@PathVariable int id) {
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }
}
