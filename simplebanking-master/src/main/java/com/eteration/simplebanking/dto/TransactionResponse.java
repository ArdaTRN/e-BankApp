package com.eteration.simplebanking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TransactionResponse {
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String approvalCode;
    public TransactionResponse(String status) {
        this.status = status;
    }
}
