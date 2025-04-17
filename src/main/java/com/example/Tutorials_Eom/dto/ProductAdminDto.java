package com.example.Tutorials_Eom.dto;

import com.example.Tutorials_Eom.entity.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductAdminDto {

    private Integer productId;
    private String name;
    private String description;
    private String category;
    private BigDecimal productsPrice;
    private ProductStatus status;

    public ProductAdminDto(Integer productId, String name, String description, String category, BigDecimal productsPrice,
                           ProductStatus status) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.productsPrice = productsPrice;
        this.status = status;
    }

}
