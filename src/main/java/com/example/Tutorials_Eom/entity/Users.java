package com.example.Tutorials_Eom.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password_hash", length = 100)
    private String passwordHash;

    @Column(name = "tel", length = 10)
    private String tel;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "fname", length = 120)
    private String firstName;

    @Column(name = "lname", length = 120)
    private String lastName;

    @Column(name = "wallet", nullable = false, columnDefinition = "DECIMAL(6,2) DEFAULT 0.00")
    private BigDecimal wallet = BigDecimal.valueOf(0.00);


    @Column(name = "wallet_last_updated", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime walletLastUpdated;

    @Column(name = "hwid", length = 150)
    private String hwid;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING) // ให้เก็บ ENUM เป็น String
    @Column(name = "role", nullable = false)
    private UserRole role = UserRole.PERSON; // ค่าเริ่มต้นเป็น PERSON


}
