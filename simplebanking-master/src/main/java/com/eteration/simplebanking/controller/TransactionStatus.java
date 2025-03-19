package com.eteration.simplebanking.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionStatus {
    private String status;
    private String approvalCode;

    public TransactionStatus(String status, String approvalCode) {
        this.status = status;
        this.approvalCode = approvalCode;
    }
}
