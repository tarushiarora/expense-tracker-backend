package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This is an annotation. It tells Spring, This is a Data Access Object,
// please create and manage an instance of it (a Spring Bean) for me.
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
