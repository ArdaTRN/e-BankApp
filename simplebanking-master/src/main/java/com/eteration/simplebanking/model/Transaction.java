package com.eteration.simplebanking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
public abstract class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private String approvalCode;

    private String type;

    @PrePersist
    public void prePersist() {
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
        if (this.approvalCode == null) {
            this.approvalCode = UUID.randomUUID().toString(); // Generate unique approval code
        }
    }

    public Transaction(double amount, String type) {
        this.amount = amount;
        this.date = LocalDateTime.now();
        this.type = type;
        this.approvalCode = UUID.randomUUID().toString(); // Generate unique approval code
    }

    public abstract void apply(Account account) throws InsufficientBalanceException;
}
