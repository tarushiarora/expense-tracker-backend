package com.example.expense_tracker.service;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return categoryRepository.save(category);
    }

    // READ
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    // read(by id)
    public Optional<Category> getIdByCategory(Long id){
        return categoryRepository.findById(id);
    }

    // UPDATE

    // delete
    public boolean deleteCategory(Long id){
        if(categoryRepository.existsById(id)){
            // will check later if my transaction is having this category
            // before deleting
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
