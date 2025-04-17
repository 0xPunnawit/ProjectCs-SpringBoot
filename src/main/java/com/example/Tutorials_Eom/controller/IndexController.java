package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public String index(Model model, @SessionAttribute(value = "username", required = false) String username) {

        if (username != null) {
            model.addAttribute("username", username);
        }

        // ใช้ @Query เพื่อคำนวณเฉพาะ userId
        long userCount = userRepository.countUserIds(); // ใช้ countUserIds แทน count()
        model.addAttribute("userCount", userCount); // เพิ่มจำนวนผู้ใช้ในโมเดล

        return "index"; // ส่งกลับไปยัง view ชื่อ index
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
