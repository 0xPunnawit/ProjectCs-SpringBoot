// ตรวจสอบว่า DOM โหลดเสร็จแล้วก่อนจะใช้งาน
document.addEventListener("DOMContentLoaded", function () {
    // จัดการการคลิกปุ่มเพื่อแสดง/ซ่อนรหัสผ่าน
    document
        .getElementById("togglePassword")
        .addEventListener("click", function () {
            var passwordField = document.getElementById("password");
            var icon = this.querySelector("i");

            // ถ้ารหัสผ่านซ่อนอยู่ (type=password) ให้แสดงออกมา
            if (passwordField.type === "password") {
                passwordField.type = "text"; // เปลี่ยนเป็น text เพื่อแสดงรหัสผ่าน
                icon.classList.remove("fa-eye-slash");
                icon.classList.add("fa-eye"); // เปลี่ยนเป็น icon ตาเปิด
            } else {
                passwordField.type = "password"; // เปลี่ยนกลับไปซ่อนรหัสผ่าน
                icon.classList.remove("fa-eye");
                icon.classList.add("fa-eye-slash"); // เปลี่ยนเป็น icon ตาปิด
            }
        });
});

function capitalizeFirstLetter(input) {
        let value = input.value;
        input.value = value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
    }

// ตรวจสอบให้กรอกเฉพาะตัวเลข
    document.getElementById('telnumber').addEventListener('input', function(e) {
        // ดึงค่าจาก input
        var input = e.target;
        var value = input.value;

        // ลบอักขระที่ไม่ใช่ตัวเลข
        input.value = value.replace(/[^0-9]/g, '');

        // ตรวจสอบให้กรอกได้เฉพาะ 10 ตัวเลข
        if (input.value.length > 10) {
            input.value = input.value.slice(0, 10);
        }
    });


document.querySelector('form').addEventListener('submit', function (e) {
  const emailInput = document.getElementById('email');
  const maxLength = 85;

  if (emailInput.value.length > maxLength) {
    e.preventDefault(); // ป้องกันการ Submit
    alert('Email must not exceed 70 characters.');
  }
});


  // ฟังก์ชันสำหรับตรวจสอบความยาว
  function validateInputLength(input, maxLength) {
    if (input.value.length > maxLength) {
      alert(`Input must not exceed ${maxLength} characters.`);
      input.value = input.value.substring(0, maxLength); // ตัดความยาวเกินออก
    }
  }


 // ตรวจสอบไฟล์ก่อนการอัปโหลด
        function validateFileInput() {
            const fileInput = document.getElementById("slipUpload");
            const file = fileInput.files[0];

            if (!file) {
                alert("กรุณาเลือกไฟล์!");
                return false;
            }

            const validImageTypes = ["image/jpeg", "image/png"];
            if (!validImageTypes.includes(file.type)) {
                alert("อนุญาตเฉพาะไฟล์รูปภาพ JPEG หรือ PNG เท่านั้น!");
                return false;
            }

            const maxSizeInMB = 3;
            if (file.size > maxSizeInMB * 1024 * 1024) {
                alert("ขนาดไฟล์ต้องไม่เกิน 3MB!");
                return false;
            }

            return true;
        }

//        ยืนยันการกดซื้อสินค้า
function validatePurchaseForm() {
        const confirmation = confirm("คุณต้องการซื้อสินค้านี้หรือไม่?");
        if (!confirmation) {
            return false;
        }

        const productIdInput = document.querySelector('input[name="productId"]');
        const userIdInput = document.querySelector('input[name="userId"]');

        if (!productIdInput || !userIdInput || !productIdInput.value || !userIdInput.value) {
            alert("ข้อมูลไม่ครบถ้วน กรุณาลองใหม่!");
            return false;
        }
        return true;
    }

