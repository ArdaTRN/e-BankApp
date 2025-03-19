package com.eteration.simplebanking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class WithdrawalTransaction extends Transaction {

    public WithdrawalTransaction(double amount) {
        super(amount, "WithdrawalTransaction"); // type of transaction defines in here.
    }

    @Override
    public void apply(Account account) throws InsufficientBalanceException {
        account.withdraw(getAmount());
    }
}
