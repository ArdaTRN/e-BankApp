package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DepositService {
    private final AccountRepository accountRepository;

    public String deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(amount, "DepositTransaction");
        account.getTransactions().add(transaction);
        accountRepository.save(account);
        // Generate and return the approval code
        return transaction.getApprovalCode(); // Example approval code generation
    }

}
