package com.example.Tutorials_Eom.controller;

import org.springframework.stereotype.Controller;
import com.example.Tutorials_Eom.entity.Category;
import com.example.Tutorials_Eom.service.CategoryService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Show Category
    @GetMapping("/admin/category")
    public String viewCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories); // ส่ง categories ไปยัง template
        System.out.println(categories);
        return "admin/category"; // ชื่อไฟล์ HTML ที่อยู่ในโฟลเดอร์ admin
    }

    // การเพิ่ม category INSERT ใหม่
    @PostMapping("/admin/category/add")
    public String addCategory(@RequestParam("categoryName") String categoryName,
                              RedirectAttributes redirectAttributes) {
        categoryService.addCategory(categoryName); // เพิ่ม category ใหม่
        redirectAttributes.addFlashAttribute("successMessage", "บันทึกสำเร็จแล้ว");
        return "redirect:/admin/category"; // Redirect กลับไปที่หน้าเดียวกันหลังจากเพิ่มข้อมูล
    }

    @GetMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id); // ลบประเภทสินค้า
            redirectAttributes.addFlashAttribute("successMessage", "ลบหมวดหมู่สินค้าสำเร็จ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาดในการลบ: " + e.getMessage());
        }
        return "redirect:/admin/category";
    }

    @GetMapping("/admin/category/edit/{id}")
    public String editCategory(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.findById(id);

        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            model.addAttribute("categoryId", category.getCategoryId());
            model.addAttribute("categoryName", category.getCategoryName());
            model.addAttribute("categories", categoryService.getAllCategories()); // โหลด categories ทั้งหมด
            return "admin/category"; // โหลดหน้าเดิมพร้อมข้อมูลที่ต้องแก้ไข
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบหมวดหมู่สินค้าที่ต้องการแก้ไข!");
            return "redirect:/admin/category";
        }
    }

    @PostMapping("/admin/category/update")
    public String updateCategory(
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("categoryName") String categoryName,
            RedirectAttributes redirectAttributes) {

        try {
            categoryService.updateCategory(categoryId, categoryName);
            redirectAttributes.addFlashAttribute("successMessage", "อัปเดตหมวดหมู่สินค้าสำเร็จ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
        }

        return "redirect:/admin/category";
    }


}
