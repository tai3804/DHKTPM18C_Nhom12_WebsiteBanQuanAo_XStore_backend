package iuh.fit.xstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private LocalDate dob;
    private String email;
    private String phone;
    private String avatar;

    // Số điện thoại đã được xác thực qua OTP chưa?
//    @Column(name = "is_phone_verified")
//    @Builder.Default
//    private boolean isPhoneVerified = false;

    @Column(name = "is_phone_verified")
    @Builder.Default
    private Boolean isPhoneVerified = false;


    @Builder.Default
    private int point = 0; // diem de xet loai user

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)

    @Builder.Default
    private UserType userType = UserType.COPPER; // loai user (tich diem) dong - bac - vang

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    // One-to-Many relationship với ShipInfo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipInfo> shipInfos;
}
