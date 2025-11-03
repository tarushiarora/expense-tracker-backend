package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.model.TransactionType;
import com.example.expense_tracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // creating a new category
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        Category newCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    // get all categories
   @GetMapping
   public List<Category> getAllCategories(){
       return categoryService.getAllCategories();
   }
    // read category by id
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Optional<Category> categoryOptional = categoryService.getCategoryById(id);

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
        return categoryService.getCategoriesByType(type);
    }

    // UPDATE an existing category
    // PUT http://localhost:8080/api/categories/1
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Optional<Category> updatedCategoryOptional = categoryService.updateCategory(id, categoryDetails);

        if (updatedCategoryOptional.isPresent()) {
            return ResponseEntity.ok(updatedCategoryOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        // DELETE a category
        // DELETE http://localhost:8080/api/categories/1
        @DeleteMapping("/{id}")
        public ResponseEntity<HttpStatus> deleteCategory(@PathVariable Long id) {
            if (categoryService.deleteCategory(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

}
