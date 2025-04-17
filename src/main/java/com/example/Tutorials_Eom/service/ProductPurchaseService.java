package com.example.Tutorials_Eom.service;

import com.example.Tutorials_Eom.dto.ProductDto;
import com.example.Tutorials_Eom.dto.UserWalletDto;
import com.example.Tutorials_Eom.entity.*;
import com.example.Tutorials_Eom.repository.OrderRepository;
import com.example.Tutorials_Eom.repository.ProductRepository;
import com.example.Tutorials_Eom.repository.UserProductsRepository;
import com.example.Tutorials_Eom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ProductPurchaseService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository ordersRepository;

    @Autowired
    private UserProductsRepository userProductsRepository; // เพิ่ม Repository

    public OrdersStatus purchaseProduct(Integer userId, Integer productId) {
        // ดึงเฉพาะข้อมูลที่จำเป็นของ Product
        ProductDto productDto = productRepository.findProductDetailsById(productId);
        if (productDto == null) {
            throw new RuntimeException("Product not found");
        }

        // ดึงข้อมูลผู้ใช้
        UserWalletDto userDto = userRepository.findUserWalletDtoByUserId(userId);
        if (userDto == null) {
            throw new RuntimeException("User not found");
        }

        // ตรวจสอบยอดเงินในกระเป๋า
        if (userDto.getWallet().compareTo(productDto.getProductsPrice()) < 0) {
            return OrdersStatus.CANCELLED; // ยอดเงินไม่พอ
        }

        // อัปเดตยอดเงินในกระเป๋าและเวลาล่าสุด
        BigDecimal newWallet = userDto.getWallet().subtract(productDto.getProductsPrice());
        userRepository.updateUserWallet(userId, newWallet, LocalDateTime.now());

        // บันทึกคำสั่งซื้อ
        Orders order = new Orders()
                .setUser(new Users().setUserId(userId)) // ใช้แค่ userId
                .setProduct(new Product().setProductId(productId)) // ใช้แค่ productId
                .setAmount(productDto.getProductsPrice())
                .setOrderDate(LocalDateTime.now())
                .setStatus(OrdersStatus.COMPLETED);

        ordersRepository.save(order);

        // *ันทึกข้อมูลลง user_products
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        UserProducts userProduct = new UserProducts()
                .setUser(new Users().setUserId(userId))
                .setProduct(product)
                .setStatus(UserProductsStatus.ACTIVATE)
                .setStartDate(LocalDateTime.now())
                .setEndDate(LocalDateTime.now().plusDays(product.getRentalDays())); // ใช้ rentalDays จาก Product

        userProductsRepository.save(userProduct); // บันทึกลงตาราง user_products

        return OrdersStatus.COMPLETED;
    }


}