package com.example.Tutorials_Eom.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.Duration;

@Data
public class UserProductDTO {
    private Integer userProductsId;
    private Integer productId;
    private String username;
    private Double wallet;
    private String name;
    private String fileProgram;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private String remainingTime;
    private long totalDays;

    public UserProductDTO(Integer userProductsId, Integer productId, String username, Double wallet, String name,
                          String fileProgram, LocalDateTime startDate, LocalDateTime endDate, String status) {
        this.userProductsId = userProductsId;
        this.productId = productId;
        this.username = username;
        this.wallet = wallet;
        this.name = name;
        this.fileProgram = fileProgram;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.remainingTime = calculateRemainingTime();
        this.totalDays = calculateTotalDays();
    }

    private String calculateRemainingTime() {
        Duration duration = Duration.between(LocalDateTime.now(), this.endDate);
        long days = duration.toDays();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        if (duration.isNegative()) {
            return "หมดอายุ";
        }

        return days + " days " + hours + " hours " + minutes + " minutes";
    }

    private long calculateTotalDays() {
        Duration duration = Duration.between(this.startDate, this.endDate);
        return duration.toDays(); // คำนวณจำนวนวันทั้งหมด
    }
}
