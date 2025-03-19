package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountNumber}/credit")
    public ResponseEntity<TransactionStatus> credit(@PathVariable String accountNumber, @RequestBody DepositTransaction transaction) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        account.post(transaction);
        accountService.saveAccount(account);  // Save the updated account.
        return ResponseEntity.ok(new TransactionStatus("OK", transaction.getApprovalCode()));
    }

    @PostMapping("/{accountNumber}/debit")
    public ResponseEntity<TransactionStatus> debit(@PathVariable String accountNumber, @RequestBody WithdrawalTransaction transaction) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) return ResponseEntity.notFound().build();

        try {
            account.post(transaction);
            accountService.saveAccount(account);
            return ResponseEntity.ok(new TransactionStatus("OK", transaction.getApprovalCode()));
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(new TransactionStatus("FAILED: " + e.getMessage(), transaction.getApprovalCode()));
        }
    }
}
