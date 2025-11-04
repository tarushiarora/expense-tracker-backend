package com.example.expense_tracker.service;


import com.example.expense_tracker.dto.MonthlySummaryDTO;
import com.example.expense_tracker.model.Transaction;
import com.example.expense_tracker.model.TransactionType;
import com.example.expense_tracker.repository.TransactionRepository;
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

    // constructor injection.
    // makes sure service always has its repository.
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // CREATE
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // READ
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // READ( GET BY ID)
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    // UPDATE
    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id" + id));
        // updating transaction details
        existing.setAmount(transactionDetails.getAmount());
        existing.setDescription(transactionDetails.getDescription());
        existing.setDate(transactionDetails.getDate());
        existing.setCategory(transactionDetails.getCategory());

        return transactionRepository.save(existing);
    }

    //DELETE
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id " + id);
        }
        transactionRepository.deleteById(id);
    }

    // METHOD TO CALCULATE MONTHLY SUMMARY

    public MonthlySummaryDTO getMonthlySummary(YearMonth yearMonth){
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // calling new repo for INCOME
        BigDecimal totalIncome = transactionRepository.findTotalAmountByTypeAndDateRange(
                TransactionType.INCOME,
                startDate,
                endDate
        );

        // calling repo for expense

        BigDecimal totalExpense = transactionRepository.findTotalAmountByTypeAndDateRange(
                TransactionType.EXPENSE,
                startDate,
                endDate
        );

        // total balance
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        return new MonthlySummaryDTO(totalIncome, totalExpense, netBalance);
    }
}
