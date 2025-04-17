package com.example.Tutorials_Eom.dto;

import com.example.Tutorials_Eom.entity.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {

    private Integer productId;
    private String name;
    private BigDecimal productsPrice;
    private ProductStatus status;
    private int rentalDays;

    public ProductDto(Integer productId, String name, BigDecimal productsPrice, ProductStatus status, int rentalDays) {
        this.productId = productId;
        this.name = name;
        this.productsPrice = productsPrice;
        this.status = status;
        this.rentalDays = rentalDays;
    }
}
