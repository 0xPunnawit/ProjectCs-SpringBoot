package com.example.Tutorials_Eom.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {

    public static String encodePassword(String password) {
        try {
            // สร้าง MessageDigest สำหรับ SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // เข้ารหัสรหัสผ่าน
            byte[] encodedHash = digest.digest(password.getBytes());

            // แปลงเป็นรูปแบบ Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString(); // คืนค่ารหัสผ่านที่เข้ารหัสแล้ว
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }
}
