package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.dto.CreditRequest;
import com.eteration.simplebanking.dto.TransactionResponse;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.services.AccountService;
import com.eteration.simplebanking.services.DepositService;
import com.eteration.simplebanking.services.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final DepositService depositService;
    private final WithdrawService withdrawService;

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        // Sort the transactions by date in descending order (newest first)
        account.getTransactions().sort(Comparator.comparing(Transaction::getDate).reversed());

        return ResponseEntity.ok(account);
    }



    @PostMapping("/{accountNumber}/credit")
    public ResponseEntity<TransactionResponse> credit(@PathVariable String accountNumber, @RequestBody CreditRequest creditRequest) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build(); // Return 404 if account is not found
        }

        // Process the deposit and generate the approval code
        String approvalCode = depositService.deposit(accountNumber,creditRequest.getAmount()); // Assuming this returns the approval code

        // Create the TransactionResponse with status and approvalCode
        TransactionResponse response = new TransactionResponse("OK", approvalCode);

        // Return the response with HTTP status 200 OK
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{accountNumber}/debit")
    public ResponseEntity<TransactionResponse> debit(@PathVariable String accountNumber, @RequestBody CreditRequest creditRequest) {
        Account account = accountService.findAccount(accountNumber);
        if (account == null) {
            return ResponseEntity.notFound().build(); // Return 404 if account is not found
        }

        try {
            // Process the withdrawal and generate the approval code
            String approvalCode = withdrawService.withdraw(accountNumber, creditRequest.getAmount()); // Assuming this returns the approval code

            // Create the TransactionResponse with status and approvalCode for success
            TransactionResponse response = new TransactionResponse("OK", approvalCode);

            // Return the response with HTTP status 200 OK
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Return the error response without an approval code (approvalCode is not needed in error cases)
            TransactionResponse response = new TransactionResponse(e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }





}
