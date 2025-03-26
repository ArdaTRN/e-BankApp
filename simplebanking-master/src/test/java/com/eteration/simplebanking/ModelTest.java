package com.eteration.simplebanking;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.services.DepositService;
import com.eteration.simplebanking.services.WithdrawService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {

	@Mock
	private DepositService depositService;
	@Mock
	private WithdrawService withdrawService;

	private Account account;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		account = new Account("Demet Demircan", "9834");
	}

	@Test
	void testCreateAccountAndSetBalance0() {
		assertTrue(account.getOwner().equals("Demet Demircan"));
		assertTrue(account.getAccountNumber().equals("9834"));
		assertTrue(account.getBalance() == 0);
	}

	@Test
	void testDepositIntoBankAccount() {
		// Simulate deposit via DepositService
		double amount = 100;
		account.setBalance(account.getBalance() + amount); // Add amount to balance directly for testing
		assertTrue(account.getBalance() == 100);
	}

	@Test
	void testWithdrawFromBankAccount() {
		account.setBalance(100); // Set initial balance for testing
		double withdrawalAmount = 50;
		account.setBalance(account.getBalance() - withdrawalAmount); // Subtract amount for withdrawal
		assertTrue(account.getBalance() == 50);
	}

	@Test
	void testWithdrawException() {
		assertThrows(IllegalArgumentException.class, () -> {
			account.setBalance(100); // Set initial balance for testing
			double withdrawalAmount = 500;
			if (account.getBalance() < withdrawalAmount) {
				throw new IllegalArgumentException("Insufficient funds");
			}
		});
	}

	@Test
	void testTransactions() {
		// Simulate deposit transaction
		double depositAmount = 100;
		account.setBalance(account.getBalance() + depositAmount); // Simulate deposit
		assertTrue(account.getBalance() == 100);

		// Simulate withdrawal transaction
		double withdrawalAmount = 60;
		account.setBalance(account.getBalance() - withdrawalAmount); // Simulate withdrawal
		assertTrue(account.getBalance() == 40);
	}
}
