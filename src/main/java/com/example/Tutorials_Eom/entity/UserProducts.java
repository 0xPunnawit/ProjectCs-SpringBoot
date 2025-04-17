package com.example.Tutorials_Eom.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "user_products")
public class UserProducts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userProductsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 25)
    private UserProductsStatus status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;


    @Transient
    public Long getTotalDays() {
        if (startDate != null && endDate != null) {
            return java.time.Duration.between(startDate, endDate).toDays();
        }
        return null; // หรือ return 0 ถ้า prefer แบบนี้
    }


}