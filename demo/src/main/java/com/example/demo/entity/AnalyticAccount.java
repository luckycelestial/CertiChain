package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "analytic_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Analytic account code is required")
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @NotBlank(message = "Analytic unit name is required")
    @Column(nullable = false)
    private String name; // e.g., "Enterprise Verification API Sector"

    private String description;
}
