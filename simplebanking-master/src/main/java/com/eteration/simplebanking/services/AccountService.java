package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.Account;
import com.eteration.simplebanking.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Find account by given account number.
    public Account findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // Saving the account.
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

}
