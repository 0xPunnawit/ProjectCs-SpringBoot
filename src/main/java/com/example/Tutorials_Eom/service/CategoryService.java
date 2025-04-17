package com.example.Tutorials_Eom.service;


import com.example.Tutorials_Eom.entity.Category;
import com.example.Tutorials_Eom.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ดึงข้อมูลทั้งหมด
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Order.asc("categoryId")));
    }

    // INSERT category ใหม่
    public void addCategory(String categoryName) {
        if (categoryRepository.findAll().stream().anyMatch(c -> c.getCategoryName().equalsIgnoreCase(categoryName))) {
            throw new RuntimeException("Category already exists!");
        }
        Category category = new Category();
        category.setCategoryName(categoryName);
        categoryRepository.save(category);
    }

    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepository.findAll()
                .stream()
                .filter(c -> c.getCategoryName().equalsIgnoreCase(categoryName))
                .findFirst();
    }

    public Optional<Category> findById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public void deleteCategory(Integer categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found!");
        }
        categoryRepository.deleteById(categoryId);
    }

    public void updateCategory(Integer categoryId, String categoryName) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        category.setCategoryName(categoryName);
        categoryRepository.save(category);
    }



}
