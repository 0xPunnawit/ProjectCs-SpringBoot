package com.example.Tutorials_Eom.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserLoginHomeDto {
    private Integer userId;
    private String username;
    private BigDecimal wallet;


    public UserLoginHomeDto(Integer userId, String username, BigDecimal wallet) {
        this.userId = userId;
        this.username = username;
        this.wallet = wallet;
    }
}
