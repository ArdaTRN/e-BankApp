package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.Transaction;
import com.eteration.simplebanking.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WithdrawService {
    private final AccountRepository accountRepository;
    public String withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber);

        // Check if the balance is sufficient for the withdrawal
        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Subtract the amount from the balance
        account.setBalance(account.getBalance() - amount);

        // Create a new withdrawal transaction
        Transaction transaction = new Transaction(amount, "WithdrawTransaction");
        account.getTransactions().add(transaction); // Add transaction to the account's list of transactions

        // Save the account with the updated balance and transaction
        accountRepository.save(account);

        // Return the approval code of the withdrawal transaction
        return transaction.getApprovalCode();
    }

}
