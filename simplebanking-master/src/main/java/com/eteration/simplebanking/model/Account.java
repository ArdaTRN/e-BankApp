package com.eteration.simplebanking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // for keeping track of the accounts.
    private String owner;
    private String accountNumber;
    private double balance;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount > this.balance) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.balance -= amount;
    }

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transaction.apply(this); // using polymorphism in here.
        transactions.add(transaction);
    }
}
