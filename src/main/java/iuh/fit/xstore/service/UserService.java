package iuh.fit.xstore.service;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepo.findAll();
    }


    public User findById(int id) {
        return userRepo.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepo.findByAccountUsername((username))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        return userRepo.searchUsers(query.toLowerCase());
    }

    //tao user

    public User createUser(User user) {
        // --- Kiểm tra tài khoản đã tồn tại ---
        if (userRepo.existsByAccountUsername(user.getAccount().getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        // --- Tạo mới Account ---
        Account account = user.getAccount();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getRole().equals(Role.ADMIN)) {
            account.setRole(Role.ADMIN);
        }else account.setRole(Role.CUSTOMER);
        account = accountRepo.save(account);

        // --- Tạo giỏ hàng mặc định ---
        Cart cart = Cart.builder()
                .total(0)
                .build();

        // --- Tạo mới User ---
        User newUser = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dob(user.getDob())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userType(UserType.COPPER)
                .point(0)
                .account(account)
                .cart(cart)
                .build();

        return userRepo.save(newUser);
    }



    // Cập nhật user
    public User updateUser(int id, User user) {
        User existedUser = userRepo.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        existedUser.setFirstName(user.getFirstName());
        existedUser.setLastName(user.getLastName());
        existedUser.setEmail(user.getEmail());
        existedUser.setPhone(user.getPhone());
        existedUser.setAvatar(user.getAvatar());

        return userRepo.save(existedUser);
    }

    // Xoá user
    public int deleteUser(int id) {
        findById(id);
        userRepo.deleteById(id);
        return id;
    }
}
