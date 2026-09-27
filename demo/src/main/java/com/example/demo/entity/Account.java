package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Account code is required")
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank(message = "Account name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    @Builder.Default
    @Column(precision = 14, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    public enum AccountType {
        ASSET,      // Cash, Bank, Cryptographic IP Infrastructure
        LIABILITY,  // Cloud Vendor Payables
        EQUITY,     // Retained Earnings, Owner Equity
        INCOME,     // Verification API Service Revenue, Certificate Issuance Revenue
        EXPENSE     // Cloud Infrastructure Expenses, Server Hosting Expenses
    }
}
