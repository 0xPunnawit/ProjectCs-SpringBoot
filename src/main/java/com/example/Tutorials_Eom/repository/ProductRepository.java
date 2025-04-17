package com.example.Tutorials_Eom.repository;

import com.example.Tutorials_Eom.dto.ProductAdminDto;
import com.example.Tutorials_Eom.dto.ProductDto;
import com.example.Tutorials_Eom.entity.Product;
import com.example.Tutorials_Eom.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByProductId(Integer Integer);

    // ============== ใช้งาน ProductList ==============
    @Query("SELECT new com.example.Tutorials_Eom.dto.ProductDto(p.productId, p.name, p.productsPrice, p.status, p.rentalDays) FROM Product p")
    List<ProductDto> findByJpqlProductList();

    // Query เพื่อดึงข้อมูลทั้งหมด
    @Query("SELECT new com.example.Tutorials_Eom.dto.ProductAdminDto(p.productId, p.name, p.description, p.category.categoryName, p.productsPrice, p.status) "
            +
            "FROM Product p")
    List<ProductAdminDto> findListProducts();

    @Query("SELECT new com.example.Tutorials_Eom.dto.ProductDto(p.productId, p.name, p.productsPrice, p.status, p.rentalDays) "
            +
            "FROM Product p WHERE p.productId = :productId")
    ProductDto findProductDetailsById(@Param("productId") Integer productId);


    // ค้นหาสินค้าจากชื่อ (Ignore Case)
    List<Product> findByNameContainingIgnoreCase(String name);


    // ใช้ ENUM `ProductStatus` และดึงค่าตาม `ProductDto`
    @Query("SELECT new com.example.Tutorials_Eom.dto.ProductDto(p.productId, p.name, p.productsPrice, p.status, p.rentalDays) FROM Product p WHERE p.status = :status")
    List<ProductDto> findByStatus(@Param("status") ProductStatus status);

}
