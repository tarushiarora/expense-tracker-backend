package com.example.expense_tracker.service;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.model.TransactionType;
import com.example.expense_tracker.model.User;
import com.example.expense_tracker.repository.CategoryRepository;
import com.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository){
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // CRUD operations

    // CREATE: link category to authenticated user
    public Category createCategoryForUser(Category category, String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        category.setUser(user);
        return categoryRepository.save(category);
    }

    // READ
    public List<Category> getCategoriesByUsername(String username){
        return categoryRepository.findByUserUsername(username);
    }

    // read(by id)
    public Optional<Category> getCategoryByIdForUser(Long id, String username){
        return categoryRepository.findById(id)
                .filter(category -> category.getUser().getUsername().equals(username));
    }

    // read ( get by type)
    public List<Category> getCategoriesByTypeAndUser(TransactionType type, String username) {
        return categoryRepository.findByTypeAndUserUsername(type, username);
    }

    // UPDATE
    public Optional<Category> updateCategoryForUser(Long id, Category categoryDetails, String username){
        return getCategoryByIdForUser(id, username).map(existingCategory -> {
            existingCategory.setName(categoryDetails.getName());
            existingCategory.setType(categoryDetails.getType());
            return categoryRepository.save(existingCategory);
        });
    }

    // delete
    public boolean deleteCategoryForUser(Long id, String username){
        return getCategoryByIdForUser(id, username).map(category -> {
            categoryRepository.delete(category);
            return true;
        }).orElse(false);
    }
}
