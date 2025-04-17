package com.example.Tutorials_Eom.service;

import com.example.Tutorials_Eom.dto.ProductAdminDto;
import com.example.Tutorials_Eom.dto.ProductDto;
import com.example.Tutorials_Eom.entity.Product;
import com.example.Tutorials_Eom.entity.ProductStatus;
import com.example.Tutorials_Eom.repository.ProductRepository;
import com.example.Tutorials_Eom.repository.UserProductsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Autowired
    private UserProductsRepository userProductsRepository;


    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    public Product saveProduct(Product product) {
        return productRepository.save(product); // ใช้ save() ที่รองรับทั้ง INSERT และ UPDATE
    }

    // List Id
    public Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<ProductAdminDto> getListProducts() {
        return productRepository.findListProducts();
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Product updateProduct(Integer id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setProductsPrice(updatedProduct.getProductsPrice());
                    existingProduct.setCategory(updatedProduct.getCategory());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }


    public boolean canUserDownload(Integer userId, Integer productId) {
        // ตรวจสอบว่าสินค้าที่ผู้ใช้พยายามดาวน์โหลดมีสถานะ "ACTIVATE"
        return userProductsRepository.existsByUserIdAndProductIdAndStatus(userId, productId, "ACTIVATE");
    }

    public String getFilePath(Integer productId) {
        // ค้นหา Product ในฐานข้อมูล และคืนค่า fileProgram
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return product.getFileProgram(); // คืนค่าเส้นทางไฟล์
    }


    //  ค้นหาสินค้าโดยใช้คำค้นหา (Ignore Case)
    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }




}