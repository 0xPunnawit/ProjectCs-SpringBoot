package com.example.Tutorials_Eom.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // ลบข้อมูล session ทั้งหมด
        return "redirect:/login"; // กลับไปหน้า login
    }

}
