package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Redirect หากยังไม่ได้ Login
        }

        UserLoginHomeDto userInfo = userRepository.findUserDTOByUserId(user.getUserId());

        // Data userId, username, wallet
        model.addAttribute("userInfo", userInfo);

        // Count UserAll
        long userCount = userRepository.countUserIds(); // ใช้ countUserIds แทน count()
        model.addAttribute("userCount", userCount);

        return "home"; // ชี้ไปยังไฟล์ home.html
    }

}
