package com.example.expense_tracker.service;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    // CRUD operations

    // CREATE
    public Category createCategory(Category category){
        return CategoryRepository.save(category);
    }

}
