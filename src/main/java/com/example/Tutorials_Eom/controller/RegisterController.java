package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository; // Inject Repository

    @GetMapping
    public String showRegistrationForm() {
        return "register"; // แสดงฟอร์มสมัครสมาชิก
    }

    @PostMapping
    public String processRegistration(
            @RequestParam("fname") String firstName,
            @RequestParam("lname") String lastName,
            @RequestParam("telnumber") String telNumber,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("cpassword") String confirmPassword,
            Model model,
            RedirectAttributes redirectAttributes

    ) {
        // ตรวจสอบข้อมูลทั้งหมด
        String errorMessage = validateUserInput(firstName, lastName, telNumber, email, username, password,
                confirmPassword);
        if (errorMessage != null) {
            setModelAttributes(model, firstName, lastName, telNumber, email, username, password, confirmPassword,
                    errorMessage);
            return "register"; // กลับไปยังฟอร์มพร้อมข้อความผิดพลาด
        }

        // ตรวจสอบความซ้ำในฐานข้อมูล
        if (userRepository.existsByUsername(username)) {
            setModelAttributes(model, firstName, lastName, telNumber, email, username, password, confirmPassword,
                    "Username already exists.");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            setModelAttributes(model, firstName, lastName, telNumber, email, username, password, confirmPassword,
                    "Email is already registered.");
            return "register";
        }

        if (userRepository.existsByTel(telNumber)) {
            setModelAttributes(model, firstName, lastName, telNumber, email, username, password, confirmPassword,
                    "Phone number is already in use.");
            return "register";
        }

        // เข้ารหัสรหัสผ่าน
        String hashedPassword = PasswordEncoder.encodePassword(password);

        // สร้างผู้ใช้ใหม่
        Users user = new Users()

                .setFirstName(firstName)
                .setLastName(lastName)
                .setTel(telNumber)
                .setEmail(email)
                .setUsername(username)
                .setPasswordHash(hashedPassword)
                .setCreatedAt(LocalDateTime.now());

        try {
            userRepository.save(user);
            return "registration-success"; // ไปที่หน้าแจ้งเตือนสำเร็จ

        } catch (Exception e) {
            e.printStackTrace();
            return "register"; // ส่งกลับไปที่หน้า register หากเกิดข้อผิดพลาด
        }

    }

    // ตรวจสอบข้อมูลผู้ใช้
    private String validateUserInput(String firstName, String lastName, String telNumber, String email, String username,
                                     String password, String confirmPassword) {
        if (!firstName.matches("[A-Za-z]+") || !lastName.matches("[A-Za-z]+")) {
            return "Name must contain English letters only.";
        }

        if (telNumber.length() != 10 || !telNumber.matches("[0-9]+")) {
            return "Phone number must be exactly 10 digits.";
        }

        if (!username.matches("[A-Za-z0-9._-]{3,20}")) {
            return "Username must be 3-20 characters (letters, numbers, ., _, -).";
        }

        // ตรวจสอบรหัสผ่าน
        if (password.length() < 5) {
            return "Password must be at least 5 characters long.";
        }

        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }

        return null; // ข้อมูลถูกต้อง
    }

    // ตั้งค่าข้อมูลใน Model
    private void setModelAttributes(Model model, String firstName, String lastName, String telNumber, String email,
                                    String username, String password, String confirmPassword, String error) {
        model.addAttribute("error", error);
        model.addAttribute("fname", firstName);
        model.addAttribute("lname", lastName);
        model.addAttribute("telnumber", telNumber);
        model.addAttribute("email", email);
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        model.addAttribute("cpassword", confirmPassword);
    }
}
