package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.entity.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TopUpController {

    @GetMapping("/top-up")
    public String showTopUpPage(HttpSession session, RedirectAttributes redirectAttributes) {
        // ตรวจสอบว่าผู้ใช้ล็อกอินหรือยัง
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // ส่งไปหน้า login
        }

        return "top-up"; // ฟอร์มใส่จำนวนเงิน
    }
}
