package com.example.expense_tracker.service;


import com.example.expense_tracker.dto.MonthlySummaryDTO;
import com.example.expense_tracker.model.Transaction;
import com.example.expense_tracker.model.TransactionType;
import com.example.expense_tracker.repository.TransactionRepository;
import com.example.expense_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // constructor injection.
    // makes sure service always has its repository.
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // helper method(to get logged in username)
    private String getCurrentUsername(){
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // CREATE
    public Transaction createTransaction(Transaction transaction) {
        // attaching logged in user to new transaction
        String username = getCurrentUsername();
        com.example.expense_tracker.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    // READ
    public List<Transaction> getAllTransactions() {
        String currentUsername = getCurrentUsername();
        return transactionRepository.findByUserUsername(currentUsername);
    }

    // READ( GET BY ID)
    public Optional<Transaction> getTransactionById(Long id) {
        // check if transaction exists and belongs to user
        return transactionRepository.findById(id)
                .filter(t->t.getUser().getUsername().equals(getCurrentUsername()));
    }

    // UPDATE
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        // ensuring user can update only their own transactions
        Transaction existing = transactionRepository.findById(id)
                .filter(t->t.getUser().getUsername().equals(getCurrentUsername()))
                .orElseThrow(() -> new RuntimeException("Transaction not found or unauthorized access"));

        // updating transaction details
        existing.setAmount(transactionDetails.getAmount());
        existing.setDescription(transactionDetails.getDescription());
        existing.setDate(transactionDetails.getDate());
        existing.setCategory(transactionDetails.getCategory());

        return transactionRepository.save(existing);
    }

    //DELETE
    public void deleteTransaction(Long id) {
        // preventing users from deleting someone else's data
        Transaction existing = transactionRepository.findById(id)
                .filter(t->t.getUser().getUsername().equals(getCurrentUsername()))
                        .orElseThrow(() -> new RuntimeException("Transaction not found or unauthorized access"));

        transactionRepository.delete(existing);
    }

    // METHOD TO CALCULATE MONTHLY SUMMARY

    public MonthlySummaryDTO getMonthlySummary(YearMonth yearMonth){
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        String username = getCurrentUsername();

        // calling new repo for INCOME
        // passing username too in these calls
        BigDecimal totalIncome = transactionRepository.findTotalAmountByTypeAndDateRange(
                TransactionType.INCOME,
                startDate,
                endDate,
                username
        );

        // calling repo for expense

        BigDecimal totalExpense = transactionRepository.findTotalAmountByTypeAndDateRange(
                TransactionType.EXPENSE,
                startDate,
                endDate,
                username
        );

        // total balance
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        return new MonthlySummaryDTO(totalIncome, totalExpense, netBalance);
    }
}
