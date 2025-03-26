package com.eteration.simplebanking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.eteration.simplebanking.controller.AccountController;

import com.eteration.simplebanking.dto.CreditRequest;
import com.eteration.simplebanking.dto.TransactionResponse;
import com.eteration.simplebanking.model.Account;

import com.eteration.simplebanking.services.AccountService;

import com.eteration.simplebanking.services.DepositService;
import com.eteration.simplebanking.services.WithdrawService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class ControllerTests {

    @Spy
    @InjectMocks
    private AccountController controller;

    @Mock
    private AccountService service;

    @Mock
    private WithdrawService withdrawService;

    @Mock
    private DepositService depositService;

    @Test
    public void givenId_Credit_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        // Mock deposit behavior
        String approvalCode = "approval123"; // Mock approval code
        doReturn(approvalCode).when(depositService).deposit("17892", 1000.0);

        ResponseEntity<TransactionResponse> result = controller.credit("17892", new CreditRequest(1000.0));

        verify(service, times(1)).findAccount("17892");
        assertEquals("OK", result.getBody().getStatus());
        assertEquals(approvalCode, result.getBody().getApprovalCode());
    }

    @Test
    public void givenId_CreditAndThenDebit_thenReturnJson() throws Exception {
        // Initialize the account with a starting balance of 0.0
        Account account = new Account("Kerem Karaca", "17892");
        account.setBalance(0.0); // Explicitly set initial balance to 0

        // Mock the service to return the created account on each call
        when(service.findAccount("17892")).thenReturn(account); // Return the same account

        // Mock deposit behavior
        String depositApprovalCode = "depositApproval123";
        doAnswer(invocation -> {
            // Update balance after deposit
            account.setBalance(account.getBalance() + 1000.0);
            return depositApprovalCode; // Return the approval code
        }).when(depositService).deposit(eq("17892"), eq(1000.0));

        // Act: Perform credit operation (deposit of 1000.0)
        ResponseEntity<TransactionResponse> result = controller.credit("17892", new CreditRequest(1000.0));

        // Assert: Check if balance is updated after credit
        assertEquals("OK", result.getBody().getStatus());
        assertEquals(1000.0, account.getBalance(), 0.001); // Account balance should be updated to 1000.0

        // Mock withdrawal behavior
        String withdrawalApprovalCode = "withdrawalApproval123";
        doAnswer(invocation -> {
            // Ensure we are modifying the account's balance correctly on withdrawal
            account.setBalance(account.getBalance() - 50.0); // Update balance after withdrawal
            return withdrawalApprovalCode; // Return the approval code
        }).when(withdrawService).withdraw(eq("17892"), eq(50.0));

        // Act: Perform debit operation (withdrawal of 50.0)
        ResponseEntity<TransactionResponse> result2 = controller.debit("17892", new CreditRequest(50.0));

        // Assert: Check if balance is updated after debit
        assertEquals("OK", result2.getBody().getStatus());
        assertEquals(950.0, account.getBalance(), 0.001); // Account balance should be 950.0 after withdrawal

        // Verify service calls
        verify(service, times(2)).findAccount("17892"); // Account find is now called twice
        verify(depositService, times(1)).deposit(eq("17892"), eq(1000.0));
        verify(withdrawService, times(1)).withdraw(eq("17892"), eq(50.0)); // Ensure withdrawal happens
    }


    @Test
    public void givenId_CreditAndThenDebitMoreGetException_thenReturnJson() throws Exception {
        // Arrange: Create account without balance (initial balance will be set by credit)
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        // Mock deposit behavior to actually update the balance
        doAnswer(invocation -> {
            account.setBalance(account.getBalance() + 1000.0);
            return "depositApproval123";
        }).when(depositService).deposit("17892", 1000.0);

        // Act: Credit the account with 1000.0
        ResponseEntity<TransactionResponse> result = controller.credit("17892", new CreditRequest(1000.0));

        // Assert: Check if the status is OK and balance is updated to 1000.0
        assertEquals("OK", result.getBody().getStatus());
        assertEquals(1000.0, account.getBalance(), 0.001);  // Balance should be 1000.0 after credit
        verify(service, times(1)).findAccount("17892");

        // Mock insufficient balance exception in WithdrawService
        doThrow(new IllegalArgumentException("Insufficient funds")).when(withdrawService).withdraw("17892", 5000.0);

        // Act: Attempt to debit more than the balance (should return 400 error response)
        ResponseEntity<TransactionResponse> response = controller.debit("17892", new CreditRequest(5000.0));

        // Assert: Check if the status is 400 and the correct error message is returned
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Insufficient funds", response.getBody().getStatus());
    }


    @Test
    public void givenId_GetAccount_thenReturnJson() throws Exception {
        Account account = new Account("Kerem Karaca", "17892");
        doReturn(account).when(service).findAccount("17892");

        ResponseEntity<Account> result = controller.getAccount("17892");

        verify(service, times(1)).findAccount("17892");
        assertEquals(account, result.getBody());
    }
}

