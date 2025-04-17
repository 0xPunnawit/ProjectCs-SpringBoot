package com.example.Tutorials_Eom.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('admin')")
public class AdminController {

    @GetMapping("/")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/addProduct")
    public String loadAddProduct() {
        return "admin/add_product";
    }

    @GetMapping("/addCategory")
    public String addCategory() {
        return "admin/category";
    }


}
