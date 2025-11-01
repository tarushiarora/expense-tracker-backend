package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.Transaction;
import com.example.expense_tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    // creating a transaction
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction){
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
    }

    // getting a transaction (reading all transactions)
    @GetMapping
    public List<Transaction> getAllTransactions(){
        return transactionService.getAllTransactions();
    }

    // getting/reading a transaction by using the id of transaction
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id){
        return transactionService.getTransactionById(id)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // updating details of a transaction
    // not just sending a transaction object , but a full http response
    // <transaction> is called a generic here
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @RequestBody Transaction transactionDetails){
        try{
            Transaction updatedTransaction = transactionService.updateTransaction(id, transactionDetails);
            return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
        }

        catch(RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // delete a transaction by id
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTransaction(@PathVariable Long id){
         try{
             transactionService.deleteTransaction(id);
             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }

         catch(RuntimeException e){
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
    }

}

