package com.example.Tutorials_Eom.service;

import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ฟังก์ชันสำหรับดึงจำนวนผู้ใช้ทั้งหมด
    public long getUserCount() {
        return userRepository.count(); // ใช้ count() ของ UserRepository เพื่อดึงจำนวนผู้ใช้
    }

    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
