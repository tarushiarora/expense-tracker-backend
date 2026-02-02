package com.example.expense_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
public class Transaction {

    // pk in table
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private LocalDate date;


    @ManyToOne(fetch=FetchType.LAZY) // MANY TRANSACTIONS CAN belong to 1 CATEGORY
    // category is the parent, transaction is the child.
    // under 1 category there should be multiple transactions
    // telling jpa to create a column named column_id
    // in our transaction table to store
    // the id of the category to which that specific transaction is linked to
    @JoinColumn(name="category_id", nullable=false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    //adding user field
    //to establish a link betweeen specific user and their transactions

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User user;

    public Transaction(){
        // default constructor
    }

   // constructor

    public Transaction(Long id, BigDecimal amount, String description, LocalDate date, Category category, User user) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
        this.user = user;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    public User getUser(){
        return user;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setUser(User user){
        this.user = user;
    }
}
