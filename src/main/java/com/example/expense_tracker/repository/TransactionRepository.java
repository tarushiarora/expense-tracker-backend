package com.example.expense_tracker.repository;

import com.example.expense_tracker.model.Transaction;
import com.example.expense_tracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// This is an annotation. It tells Spring, This is a Data Access Object,
// please create and manage an instance of it (a Spring Bean) for me.
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // find transactions where linked user's username matches thr input
    List<Transaction> findByUsername(String username);

    // updated query: so that we only count transactions belonging to currently logged in user

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
    "WHERE t.category.type = :type " +
    "AND t.date BETWEEN :startDate AND :endDate " +
    "AND t.user.username = :username")
    BigDecimal findTotalAmountByTypeAndDateRange(
            @Param("type") TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("username") String username);

    // custom query to calculate amount spent during a specific time period
//    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
//            "WHERE t.category.type = :type " +
//            "AND t.date BETWEEN :startDate AND :endDate")
//    BigDecimal findTotalAmountByTypeAndDateRange(
//            @Param("type") TransactionType type,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate);

}