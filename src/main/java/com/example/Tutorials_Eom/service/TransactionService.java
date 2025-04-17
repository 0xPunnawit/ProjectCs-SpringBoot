package com.example.Tutorials_Eom.service;

import com.example.Tutorials_Eom.entity.Transactions;
import com.example.Tutorials_Eom.entity.TransactionStatus;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.TransactionRepository;
import com.example.Tutorials_Eom.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository; // Repository สำหรับ users

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // ตรวจสอบว่ามี transactionReference ในฐานข้อมูลหรือไม่
    public boolean isTransactionReferenceExists(String transactionReference) {
        return transactionRepository.existsByTransactionReference(transactionReference);
    }

    public void saveTransactionAndUpdateWallet(Integer userId, BigDecimal amount, String paymentMethod, String reference, TransactionStatus status) {
        // บันทึกข้อมูลใน transactions
        Transactions transaction = new Transactions()
                .setUser(new Users().setUserId(userId)) // กำหนด userId
                .setAmount(amount)
                .setPaymentMethod(paymentMethod)
                .setTransactionReference(reference)
                .setStatus(status)
                .setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        // อัพเดทยอดเงินใน users
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setWallet(user.getWallet().add(amount)); // เพิ่มยอดเงินในกระเป๋า
        user.setWalletLastUpdated(LocalDateTime.now()); // บันทึกเวลาที่อัพเดทกระเป๋า
        userRepository.save(user); // บันทึกข้อมูล user
    }
}

