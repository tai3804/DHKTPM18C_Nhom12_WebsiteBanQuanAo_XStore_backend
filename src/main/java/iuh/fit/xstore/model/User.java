package iuh.fit.xstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Transient
    private String rawPassword;

    @Transient
    private Role role;

    @OneToOne
    @JoinColumn(name = "address_id")
    @JsonIgnore
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    // One-to-Many relationship với ShipInfo
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ShipInfo> shipInfos;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

}
