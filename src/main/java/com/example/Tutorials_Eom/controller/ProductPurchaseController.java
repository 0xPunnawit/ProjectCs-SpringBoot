package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.service.ProductPurchaseService;
import com.example.Tutorials_Eom.entity.OrdersStatus;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductPurchaseController {

    @Autowired
    private ProductPurchaseService productPurchaseService;

    @PostMapping("/purchase")
    public String purchaseProduct(@RequestParam Long productId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        // ตรวจสอบว่าผู้ใช้ล็อกอินหรือไม่
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "กรุณาล็อกอินก่อนซื้อสินค้า");
            return "redirect:/login";
        }

        // ดำเนินการซื้อสินค้า
        OrdersStatus status = productPurchaseService.purchaseProduct(user.getUserId(), productId.intValue());

        // จัดการผลลัพธ์
        if (status == OrdersStatus.COMPLETED) {
            redirectAttributes.addFlashAttribute("successMessage", "การซื้อสำเร็จ!");
            return "redirect:/rented-programs";
        } else if (status == OrdersStatus.CANCELLED) {
            redirectAttributes.addFlashAttribute("errorMessage", "ยอดเงินไม่เพียงพอ");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาดในการสั่งซื้อ");
        }

        return "redirect:/products-list";
    }
}
