package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Category;
import com.example.expense_tracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserUsername(String username);

    List<Category> findByTypeAndUserUsername(TransactionType type, String username);

}