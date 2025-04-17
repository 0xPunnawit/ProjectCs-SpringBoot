# 📦 ProjectCs-SpringBoot-main - Software Rental Web

ระบบเว็บไซต์ให้เช่าโปรแกรมออนไลน์ พัฒนาด้วย **Java Spring Boot**  
รองรับการสมัครสมาชิก, ซื้อโปรแกรม, เช่าโปรแกรม, อัปโหลดสลิป, ตรวจสอบสลิปด้วย SlipOK API, และระบบหลังบ้านสำหรับผู้ดูแล

---

## 📌 ฟีเจอร์หลัก

- 👥 ระบบสมัครสมาชิก / ล็อกอิน / ล็อกเอาท์
- 🔐 การเข้ารหัสรหัสผ่านแบบ SHA-256
- 💳 เติมเงินผ่านระบบตรวจสอบสลิป (SlipOK API)
- 💼 เช่าและซื้อโปรแกรมแบบมีวันหมดอายุ
- 📁 ดาวน์โหลดไฟล์โปรแกรมเฉพาะคนที่เช่า
- 🧑‍💻 ระบบผู้ดูแลเพิ่ม/ลบ/แก้ไขโปรแกรมและหมวดหมู่
- 📊 แสดงจำนวนผู้ใช้ในระบบ
- 🔎 ระบบค้นหา/กรองโปรแกรมจากสถานะ (Active, Disable)
- 🧾 ระบบตรวจสอบสลิปผ่าน SlipOK API (พร้อมตรวจสอบชื่อบัญชีผู้รับ)
- 📂 อัปโหลดไฟล์โปรแกรมขนาดสูงสุด 500MB

---

## 🛠 เทคโนโลยีที่ใช้

| ด้าน        | รายละเอียด                                                |
|-------------|-------------------------------------------------------------|
| 🔧 Backend  | Java 17, Spring Boot, Spring MVC, Spring Security          |
| 🗄 Database | XAMPP MySQL (JPA / Hibernate)                              |
| 🌐 View     | Thymeleaf + HTML5                                          |
| 🔐 API      | [SlipOK API](https://slipok.com/) สำหรับตรวจสอบสลิป        |
| 🖥 IDE       | IntelliJ IDEA                                               |
| 🧪 Build Tool | Maven                                                      |

---



## 🗄️ การตั้งค่าฐานข้อมูล

1. เปิด **XAMPP Control Panel** แล้วเปิด MySQL
2. เข้า phpMyAdmin
3. สร้างฐานข้อมูลชื่อ projectcs
4. Import ไฟล์ projectcs.sql ที่อยู่ในโฟลเดอร์ database/ ดังนี้:

   - ใช้ **phpMyAdmin**:
     > ไปที่ฐานข้อมูล projectcs → เลือกแท็บ Import → เลือกไฟล์ projectcs.sql → กด Go ✅

---

## ⚙️ การติดตั้งและรันโปรเจกต์

1. Clone โปรเจกต์นี้:
bash
git clone https://github.com/0xPunnawit/ProjectCs-SpringBoot.git

แตกไฟล์โปรเจกต์ลงในไดรฟ์ D: เท่านั้น
โดยต้องอยู่ใน path ดังนี้: D:\ProjectCs-SpringBoot-main\

🛑 สำคัญมาก: หากวางไว้ที่ path อื่น เช่น Desktop, C:\ หรือโฟลเดอร์ชื่ออื่น อาจทำให้ระบบอัปโหลดไฟล์โปรแกรมทำงานผิดพลาด

# Database
และไปที่ src/main/resources/application.properties ให้ใส่ ชื่อ database และ รหัสผ่านของคุณ
- spring.datasource.username=your_mysql_username
- spring.datasource.password=your_mysql_password 

# SlipOK API
และใส่ api ของ slip ok ของคุณ
- slipok.api.url=https://your-slipok-api.com
- slipok.api.key=your_api_key

รันโปรเจกต์ผ่าน IntelliJ IDEA
เปิดไฟล์ TutorialsEomApplication.java
คลิกขวา → เลือก Run

เปิดเว็บผ่านเบราว์เซอร์
http://localhost:8080

# Account users ชื่อผู้ใช้กับแอดมินเหมือนกัน 
- person -> username: AA1 password: AA1
- admin -> username: admin01  password: admin01
