package com.example.Tutorials_Eom.controller;

import com.example.Tutorials_Eom.dto.UserLoginHomeDto;
import com.example.Tutorials_Eom.entity.TransactionStatus;
import com.example.Tutorials_Eom.entity.Users;
import com.example.Tutorials_Eom.repository.UserRepository;
import com.example.Tutorials_Eom.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class SlipVerificationController {

    @Value("${slipok.api.url}")
    private String slipOkApiUrl;

    @Value("${slipok.api.key}")
    private String slipOkApiKey;

    private final TransactionService transactionService;

    private final UserRepository userRepository;

    public SlipVerificationController(TransactionService transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/verify-slip")
    public String verifySlip(HttpSession session,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            // ดึงข้อมูลผู้ใช้งานจาก Session
            Users sessionUser = (Users) session.getAttribute("user");
            if (sessionUser == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "กรุณาเข้าสู่ระบบก่อนอัปโหลดสลิป!");
                return "redirect:/top-up";
            }

            // ดึงข้อมูล UserLoginHomeDto
            Integer userId = sessionUser.getUserId();
            UserLoginHomeDto userDto = userRepository.findUserDTOByUserId(userId);
            if (userDto == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "ไม่พบข้อมูลผู้ใช้งาน!");
                return "redirect:/top-up";
            }

            // ตรวจสอบประเภทไฟล์
            String contentType = file.getContentType();
            System.out.println("Content Type ของไฟล์: " + contentType);
            if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
                redirectAttributes.addFlashAttribute("errorMessage", "อนุญาตเฉพาะไฟล์รูปภาพ JPEG หรือ PNG เท่านั้น!");
                return "redirect:/top-up";
            }

            // ตรวจสอบขนาดไฟล์ (ไม่เกิน 5MB)
            long maxFileSize = 3 * 1024 * 1024; // 5MB
            System.out.println("ขนาดไฟล์: " + file.getSize() + " bytes");
            if (file.getSize() > maxFileSize) {
                redirectAttributes.addFlashAttribute("errorMessage", "ขนาดไฟล์ต้องไม่เกิน 5MB!");
                return "redirect:/top-up";
            }

            // สร้าง HttpHeaders
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("x-authorization", slipOkApiKey);

            // สร้าง MultiValueMap สำหรับส่งข้อมูล
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            File tempFile = convertMultipartFileToFile(file);
            body.add("files", new FileSystemResource(tempFile));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // ส่งคำขอไปยัง Slip OK API
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> response = restTemplate.exchange(
                    slipOkApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            tempFile.delete(); // ลบไฟล์ชั่วคราว

            // ตรวจสอบผลลัพธ์
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data"); // ดึง data ออกมา

                // ตรวจสอบว่าข้อมูลสำคัญมีครบหรือไม่
                if (data == null || !data.containsKey("amount") || !data.containsKey("transRef")
                        || !data.containsKey("receiver")) {
                    throw new RuntimeException("Invalid API response: " + responseBody);
                }

                // ตรวจสอบชื่อผู้รับเงิน
                Map<String, Object> receiver = (Map<String, Object>) data.get("receiver");
                String expectedDisplayNamePartial = "<your_name_th>"; // ใช้คำสำคัญที่สำคัญ ตัวอย่าง "นาย กอไก่" ให้ตรงกับบัญชีของคุณ
                String expectedNamePartial = "<your_name_en";        // "ตัวอย่าง Mr. Korkai";

                String receivedDisplayName = (String) receiver.get("displayName");
                String receivedName = (String) receiver.get("name");

                // ตรวจสอบว่าชื่อมีบางส่วนตรงกัน
                if (!(receivedDisplayName.contains(expectedDisplayNamePartial) ||
                        receivedName.contains(expectedNamePartial))) {
                    redirectAttributes.addFlashAttribute(
                            "errorMessage",
                            "กรุณาตรวจสอบ QRCode ชื่อบัญชีไม่ถูกต้อง!!");
                    return "redirect:/top-up";
                }

                // ดึงข้อมูลที่จำเป็นจาก API
                BigDecimal amount = new BigDecimal(data.get("amount").toString());
                String transactionReference = data.get("transRef").toString();
                String receivingBank = data.get("receivingBank").toString();

                // ตรวจสอบว่ามี transactionReference นี้ในฐานข้อมูลหรือไม่
                if (transactionService.isTransactionReferenceExists(transactionReference)) {
                    redirectAttributes.addFlashAttribute("errorMessage", "สลิปนี้ถูกใช้ไปแล้ว!!");
                    return "redirect:/top-up";
                }

                // บันทึกข้อมูลลงในตาราง transactions
                transactionService.saveTransactionAndUpdateWallet(
                        sessionUser.getUserId(),
                        amount,
                        "Bank",
                        transactionReference,
                        TransactionStatus.SUCCESS);

                redirectAttributes.addFlashAttribute("successMessage",
                        "เติมเงินสำเร็จ!! ยอดเงินเพิ่ม: " + amount + " บาท");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "การตรวจสอบสลิปล้มเหลว!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "สลิปไม่ถูกต้อง!!");
        }

        return "redirect:/top-up";
    }

    // Helper method to convert MultipartFile to File
    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File tempFile = File.createTempFile("slip", ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

}
