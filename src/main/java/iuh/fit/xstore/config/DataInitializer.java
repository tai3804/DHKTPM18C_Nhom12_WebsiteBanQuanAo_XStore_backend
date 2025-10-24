package iuh.fit.xstore.config;

import iuh.fit.xstore.dto.response.AppException;
import iuh.fit.xstore.dto.response.ErrorCode;
import iuh.fit.xstore.model.*;
import iuh.fit.xstore.repository.AccountRepository;
import iuh.fit.xstore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AccountRepository accountRepo,
                               UserRepository userRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepo.existsByAccountUsername("admin")) {
                return;
            }

            User user = User.builder()
                    .firstName("admin")
                    .lastName("default")
                    .account(
                            Account.builder()
                                    .username("admin")
                                    .password(passwordEncoder.encode("admin"))
                                    .role(Role.ADMIN)
                            .build()
                    )
                    .cart(
                            Cart.builder()
                                    .total(0)
                                    .build()
                    )
                    .build();

            userRepo.save(user);
        };
    }
}
