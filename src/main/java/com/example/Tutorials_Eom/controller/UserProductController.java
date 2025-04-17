package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.dto.UserProductDTO;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.service.UserProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class UserProductController {

    @Autowired
    private UserProductService userProductService;

    @GetMapping("/rented-programs")
    public String rentedPrograms(HttpSession session, Model model) {
        // ดึงข้อมูลผู้ใช้จาก Session
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // ส่งผู้ใช้ไปหน้า Login หากยังไม่ได้ล็อกอิน
        }

        // ดึง userId จากผู้ใช้
        Integer userId = user.getUserId();

        // ดึงข้อมูลสินค้าที่เช่าของผู้ใช้จาก Service (ใช้ DTO)
        List<UserProductDTO> userProducts = userProductService.getUserProductDetails(userId);

        // เพิ่มข้อมูลลงใน Model เพื่อส่งไป View
        model.addAttribute("userProducts", userProducts);

        return "rented-programs"; // ไปที่หน้า rented-programs.html
    }

}
