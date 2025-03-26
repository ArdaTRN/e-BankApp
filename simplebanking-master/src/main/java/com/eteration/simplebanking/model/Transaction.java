package com.eteration.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    private String approvalCode;
    private double amount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private String type;
    public Transaction(double amount, String type) {
        this.approvalCode = UUID.randomUUID().toString();
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.type = type;
    }
}
