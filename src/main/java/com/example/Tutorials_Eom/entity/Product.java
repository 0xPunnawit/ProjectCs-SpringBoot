package com.example.Tutorials_Eom.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = true)
    private Category category;

    @Column(name = "name", nullable = false, length = 40)
    private String name;

    @Column(name = "description", length = 40)
    private String description;

    @Column(name = "products_price", nullable = false, precision = 6, scale = 2)
    private BigDecimal productsPrice;

    @Column(name = "product_created_at", nullable = false)
    private LocalDateTime productCreatedAt;

    @Enumerated(EnumType.STRING) // บอก Hibernate ให้เก็บ Enum เป็น String ในฐานข้อมูล
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "file_path", nullable = false, length = 255)
    private String filePath;

    @Column(name = "rental_days", nullable = false, columnDefinition = "INT DEFAULT 1")
    private Integer rentalDays;

    @Column(name = "file_program", nullable = false, length = 255)
    private String fileProgram;

}
