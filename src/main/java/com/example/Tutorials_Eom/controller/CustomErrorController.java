package com.example.Tutorials_Eom.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("error", "404");
        return "404"; // ส่งไปยังหน้า 404.html
    }
}
