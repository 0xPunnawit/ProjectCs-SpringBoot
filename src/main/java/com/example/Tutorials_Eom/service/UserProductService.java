package com.example.Tutorials_Eom.service;

import com.example.Tutorials_Eom.dto.UserProductDTO;
import com.example.Tutorials_Eom.entity.UserProducts;
import com.example.Tutorials_Eom.entity.UserProductsStatus;
import com.example.Tutorials_Eom.repository.UserProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserProductService {

    @Autowired
    private UserProductsRepository userProductsRepository;

    public List<UserProductDTO> getUserProductDetails(Integer userId) {
        return userProductsRepository.findUserProductDetailsNative(userId).stream()
                .map(result -> new UserProductDTO(
                        ((Number) result[0]).intValue(), // userProductsId
                        ((Number) result[1]).intValue(), // productId
                        (String) result[2], // username
                        ((java.math.BigDecimal) result[3]).doubleValue(), // wallet
                        (String) result[4], // name
                        (String) result[5], // fileProgram
                        result[6] != null ? ((java.sql.Timestamp) result[6]).toLocalDateTime() : null, // startDate
                        result[7] != null ? ((java.sql.Timestamp) result[7]).toLocalDateTime() : null, // endDate
                        (String) result[8] // status
                )).toList();
    }

    public boolean canUserDownload(Integer userId, Integer productId) {
        // ตรวจสอบว่าสินค้าที่ผู้ใช้พยายามดาวน์โหลดมีสถานะ "ACTIVATE"
        return userProductsRepository.existsByUserIdAndProductIdAndStatus(userId, productId, "ACTIVATE");
    }


    // Scheduled Task ที่จะรันทุกๆ 1 นาที
    @Scheduled(fixedRate = 60000) // รันทุก 1 นาที
    public void checkAndUpdateExpiredProducts() {
        // ดึงข้อมูล user_products ที่ end_date หมดอายุแล้ว
        userProductsRepository.updateExpiredStatus(LocalDateTime.now());
    }



}

