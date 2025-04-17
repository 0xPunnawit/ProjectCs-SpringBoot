document.addEventListener("DOMContentLoaded", function () {
    let searchInput = document.getElementById("searchInput");
    let suggestions = document.getElementById("suggestions");
    let searchForm = document.getElementById("searchForm");

    if (searchInput) {
        searchInput.addEventListener("keyup", function () {
            let query = searchInput.value.trim();
            suggestions.innerHTML = ""; // ล้างผลลัพธ์ก่อนหน้า
            suggestions.style.display = "none"; // ซ่อน suggestions ถ้าไม่มีอะไรค้นหา

            if (query.length >= 1) {
                fetch("/products-list/search?query=" + encodeURIComponent(query))
                    .then(response => response.json())
                    .then(data => {
                        if (data.length > 0) {
                            suggestions.style.display = "block"; // แสดงผลลัพธ์
                            data.forEach(function (productName) {
                                let item = document.createElement("a");
                                item.classList.add("list-group-item", "list-group-item-action", "search-item");
                                item.href = "#"; // ป้องกันเปลี่ยนหน้า
                                item.textContent = productName;
                                item.addEventListener("click", function (event) {
                                    event.preventDefault();
                                    searchInput.value = productName;
                                    suggestions.style.display = "none"; // ปิด Auto-Suggest
                                    searchForm.submit(); // ค้นหาสินค้านี้
                                });
                                suggestions.appendChild(item);
                            });
                        } else {
                            let noResult = document.createElement("div");
                            noResult.classList.add("list-group-item", "disabled");
                            noResult.textContent = "❌ ไม่พบสินค้า";
                            suggestions.appendChild(noResult);
                            suggestions.style.display = "block";
                        }
                    })
                    .catch(error => console.error("❌ Error fetching search results:", error));
            }
        });

        // ✅ ซ่อน Auto-Suggest เมื่อคลิกข้างนอก
        document.addEventListener("click", function (event) {
            if (!searchInput.contains(event.target) && !suggestions.contains(event.target)) {
                suggestions.style.display = "none";
            }
        });
    }
});
