package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.model.TransactionType;
import com.example.expense_tracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    // method to extract username from jwt/security context
    private String getAuthenticatedUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // creating a new category, for logged in user
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category newCategory = categoryService.createCategoryForUser(category, getAuthenticatedUsername());
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // get categories belonging to logged in user
   @GetMapping
   public List<Category> getAllCategories(){
       return categoryService.getCategoriesByUsername(getAuthenticatedUsername());
   }
    // read category by id
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Optional<Category> categoryOptional = categoryService.getCategoryByIdForUser(id, getAuthenticatedUsername());

        if(categoryOptional.isPresent()){
            return ResponseEntity.ok(categoryOptional.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    // read all categories of a specific type
    @GetMapping("/type/{type}")
    public List<Category> getCategoriesByType(@PathVariable TransactionType type){
        return categoryService.getCategoriesByTypeAndUser(type, getAuthenticatedUsername());
    }

    // UPDATE an existing category
    // PUT http://localhost:8080/api/categories/1
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Optional<Category> updatedCategoryOptional = categoryService.updateCategoryForUser(id, categoryDetails, getAuthenticatedUsername());

        if (updatedCategoryOptional.isPresent()) {
            return ResponseEntity.ok(updatedCategoryOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        // DELETE a category (making sure... user owns it)
        // DELETE http://localhost:8080/api/categories/1
        @DeleteMapping("/{id}")
        public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
            if (categoryService.deleteCategoryForUser(id, getAuthenticatedUsername())) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

}
