package com.example.Tutorials_Eom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/404")
    public String notFoundPage(Model model) {
        model.addAttribute("error", "404"); // ส่งสถานะไปยังหน้า HTML
        return "404"; // ชื่อไฟล์ HTML สำหรับหน้า 404
    }
}
