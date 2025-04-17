package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.dto.ProductAdminDto;
import com.example.Tutorials_Eom.dto.ProductDto;
import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.entity.Category;
import com.example.Tutorials_Eom.entity.Product;
import com.example.Tutorials_Eom.entity.ProductStatus;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.ProductRepository;
import com.example.Tutorials_Eom.repository.UserRepository;
import com.example.Tutorials_Eom.service.CategoryService;
import com.example.Tutorials_Eom.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Controller
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    private final CategoryService categoryService;

    public ProductsController(ProductRepository productRepository, ProductService productService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products-list")
    public String showProductsList(
            @RequestParam(value = "query", required = false) String search,
            @RequestParam(value = "status", required = false) String status,
            Model model, HttpSession session) {

        List<ProductDto> products;

        try {
            //  ถ้ามี `query` ให้ค้นหาสินค้า
            if (search != null && !search.trim().isEmpty()) {
                products = productService.searchProducts(search)
                        .stream()
                        .map(product -> new ProductDto(
                                product.getProductId(),
                                product.getName(),
                                product.getProductsPrice(),
                                product.getStatus(),
                                product.getRentalDays()
                        ))
                        .toList();
            }
            // ถ้ามี `status` และไม่ใช่ "ALL" → กรอง `status`
            else if (status != null && !status.equals("ALL")) {
                //  แปลง `String` เป็น Enum ก่อนใช้ค้นหา
                ProductStatus productStatus = ProductStatus.valueOf(status);
                products = productRepository.findByStatus(productStatus)
                        .stream()
                        .map(product -> new ProductDto(
                                product.getProductId(),
                                product.getName(),
                                product.getProductsPrice(),
                                product.getStatus(),
                                product.getRentalDays()
                        ))
                        .toList();
            }
            // ถ้าไม่มีทั้ง `query` และ `status` → โหลดสินค้าทั้งหมด
            else {
                products = productRepository.findByJpqlProductList();
            }

        } catch (IllegalArgumentException e) {
            // กรณีที่ `status` ไม่ถูกต้อง → โหลดสินค้าทั้งหมดแทน
            products = productRepository.findByJpqlProductList();
        }

        model.addAttribute("products", products);

        //  ดึงข้อมูล User
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            UserLoginHomeDto userInfo = userRepository.findUserDTOByUserId(user.getUserId());
            model.addAttribute("userInfo", userInfo);
        }

        return "products";
    }


    // view Product id
    @GetMapping("/view-product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id.intValue());

        // แปลงวันที่ที่ต้องการในรูปแบบ "วัน/เดือน/ปี"
        LocalDateTime createdAt = product.getProductCreatedAt(); // สมมุติ productCreatedAt เป็น LocalDateTime
        String formattedDate = createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        model.addAttribute("product", product);
        model.addAttribute("formattedDate", formattedDate); // ส่งวันที่แบบฟอร์แมตแล้วไป View
        return "view_product";
    }


    @GetMapping("/products")
    public String products(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        if (user != null) {
            UserLoginHomeDto userInfo = userRepository.findUserDTOByUserId(user.getUserId());

            model.addAttribute("userInfo", userInfo);
        }
        // เพิ่ม logic สำหรับ products
        return "products";
    }

    @GetMapping("/download/{id}")
    public Object downloadFile(@PathVariable Long id, HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");

        // ตรวจสอบว่าผู้ใช้ล็อกอินหรือไม่
        if (user == null) {
            model.addAttribute("errorMessage", "คุณไม่ได้รับอนุญาต");
            return "error"; // หน้า Thymeleaf error.html
        }

        // ตรวจสอบสิทธิ์การดาวน์โหลด
        if (!productService.canUserDownload(user.getUserId(), id.intValue())) {
            model.addAttribute("errorMessage", "คุณไม่ได้รับอนุญาตให้ดาวน์โหลดโปรแกรมนี้");
            return "error"; // หน้า Thymeleaf error.html
        }

        // ดึงไฟล์จากตำแหน่งใน database
        String filePath = productService.getFilePath(id.intValue());
        try {
            Resource resource = new UrlResource(Paths.get(filePath).toUri());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + Paths.get(filePath).getFileName().toString() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            model.addAttribute("errorMessage", "Error: File not found.");
            return "error"; // หน้า Thymeleaf error.html
        }
    }


    // ============================ ADMIN ============================

    // Method ดึงข้อมูลทั้งหมด (SELECT FROM product)
    @GetMapping("/admin/loadAddProduct")
    public String getAllProducts(Model model) {
        List<ProductAdminDto> products = productService.getListProducts();
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories()); //  ส่ง categories ไปที่ View
        return "admin/add_product";
    }


    @PostMapping("/admin/product/add")
    public String addProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") Integer categoryId,
            @RequestParam("status") ProductStatus status,
            @RequestParam("price") Double price,
            @RequestParam(value = "rentalDays", defaultValue = "1") Integer rentalDays,
            @RequestParam("youtubeUrl") String youtubeUrl, //  รับ URL ของ YouTube
            @RequestParam("fileProgram") MultipartFile fileProgram, //  รับไฟล์โปรแกรม
            RedirectAttributes redirectAttributes) {

        try {
            //  ดึง Category จากฐานข้อมูล
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            //  ตรวจสอบว่าไฟล์อัปโหลดมาหรือไม่
            if (fileProgram.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please upload a file.");
                return "redirect:/admin/loadAddProduct";
            }

            //  กำหนดพาธที่ใช้บันทึกไฟล์โปรแกรม
            String uploadDir = "D:/ProjectCs-SpringBoot-main/src/main/resources/files/";
            String fileName = fileProgram.getOriginalFilename();

            //  สร้างโฟลเดอร์ถ้ายังไม่มี
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            //  บันทึกไฟล์ลงโฟลเดอร์
            Path filePath = Paths.get(uploadDir + fileName);
            Files.copy(fileProgram.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            //  สร้างสินค้าใหม่ และเซ็ต `file_path` เป็น URL YouTube และ `file_program` เป็นพาธไฟล์จริง
            Product product = new Product()
                    .setName(name)
                    .setDescription(description)
                    .setCategory(category)
                    .setProductsPrice(BigDecimal.valueOf(price))
                    .setRentalDays(rentalDays)
                    .setStatus(status)
                    .setFilePath(youtubeUrl) //  เก็บ YouTube URL
                    .setFileProgram(uploadDir + fileName) //  เก็บพาธของไฟล์โปรแกรม
                    .setProductCreatedAt(LocalDateTime.now());

            //  บันทึกสินค้าในฐานข้อมูล
            productService.saveProduct(product);

            //  แจ้งเตือนว่าบันทึกสำเร็จ
            redirectAttributes.addFlashAttribute("successMessage", "บันทึกสำเร็จแล้ว");
            return "redirect:/admin/loadAddProduct";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving file: " + e.getMessage());
            return "redirect:/admin/loadAddProduct";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving product: " + e.getMessage());
            return "redirect:/admin/loadAddProduct";
        }
    }

    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(@PathVariable Integer id, Model model) {
        Product product = productService.findById(id);

        model.addAttribute("productId", product.getProductId());
        model.addAttribute("productName", product.getName());
        model.addAttribute("productDescription", product.getDescription());
        model.addAttribute("selectedCategory", product.getCategory().getCategoryId());
        model.addAttribute("selectedStatus", product.getStatus().name());
        model.addAttribute("productPrice", product.getProductsPrice());
        model.addAttribute("productYoutubeUrl", product.getFilePath());
        model.addAttribute("productRentalDays", product.getRentalDays());

        //  ดึง Categories ทั้งหมด
        model.addAttribute("categories", categoryService.getAllCategories());

        //  ดึงรายการสินค้าทั้งหมด (เพื่อให้แสดงในตารางขวา)
        model.addAttribute("products", productService.getListProducts());

        return "admin/add_product"; // โหลดหน้าเดิม พร้อมข้อมูลทั้งหมด
    }


    @PostMapping("/admin/product/update")
    public String updateProduct(
            @RequestParam("productId") Integer productId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("category") Integer categoryId,
            @RequestParam("status") ProductStatus status,
            @RequestParam("price") Double price,
            @RequestParam("rentalDays") Integer rentalDays,
            @RequestParam("youtubeUrl") String youtubeUrl,  //  เพิ่มรับค่า URL
            @RequestParam(value = "fileProgram", required = false) MultipartFile fileProgram, //  รับไฟล์ แต่ไม่จำเป็นต้องอัปโหลดใหม่
            RedirectAttributes redirectAttributes) {

        try {
            //  ดึงสินค้าเดิมจาก Database
            Product product = productService.findById(productId);
            if (product == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบสินค้าที่ต้องการอัปเดต!");
                return "redirect:/admin/loadAddProduct";
            }

            //  ดึง Category ที่เลือก
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            //  อัปเดตข้อมูลสินค้า
            product.setName(name)
                    .setDescription(description)
                    .setCategory(category)
                    .setProductsPrice(BigDecimal.valueOf(price))
                    .setRentalDays(rentalDays)
                    .setStatus(status)
                    .setFilePath(youtubeUrl); //  อัปเดต URL YouTube

            //  เช็คว่ามีไฟล์ใหม่ถูกอัปโหลดหรือไม่
            if (fileProgram != null && !fileProgram.isEmpty()) {
                String uploadDir = "D:/ProjectCs-SpringBoot-main/src/main/resources/files/";
                String fileName = fileProgram.getOriginalFilename();

                //  บันทึกไฟล์ใหม่
                Path filePath = Paths.get(uploadDir + fileName);
                Files.copy(fileProgram.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                //  อัปเดตพาธของไฟล์ใหม่ลง Database
                product.setFileProgram(uploadDir + fileName);
            }

            //  บันทึกการเปลี่ยนแปลง
            productService.saveProduct(product);

            //  ส่งข้อความแจ้งเตือน
            redirectAttributes.addFlashAttribute("successMessage", "อัปเดตสินค้าสำเร็จ!");
            return "redirect:/admin/loadAddProduct";

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating file: " + e.getMessage());
            return "redirect:/admin/loadAddProduct";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product: " + e.getMessage());
            return "redirect:/admin/loadAddProduct";
        }
    }

    // ลบข้อมูลสินค้า
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("successMessage", "ลบสินค้าสำเร็จ!");
        return "redirect:/admin/loadAddProduct";
    }




}



