package com.example.Tutorials_Eom.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserWalletDto {
    private Integer userId;
    private String username;
    private BigDecimal wallet;
    private LocalDateTime walletLastUpdated;

    public UserWalletDto(Integer userId, String username, BigDecimal wallet, LocalDateTime walletLastUpdated) {
        this.userId = userId;
        this.username = username;
        this.wallet = wallet;
        this.walletLastUpdated = walletLastUpdated;
    }

}
