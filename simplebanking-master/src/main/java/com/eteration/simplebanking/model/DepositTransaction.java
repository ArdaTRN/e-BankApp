package com.eteration.simplebanking.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor
public class DepositTransaction extends Transaction {

    public DepositTransaction(double amount) {
        super(amount, "DepositTransaction"); // type of transaction defines in here.
    }

    @Override
    public void apply(Account account) {
        account.deposit(getAmount());
    }
}
