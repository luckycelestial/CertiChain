package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Budget title is required")
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "analytic_account_id", nullable = false)
    private AnalyticAccount analyticAccount;

    @NotNull(message = "Planned revenue is required")
    @Column(name = "planned_revenue", precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal plannedRevenue = BigDecimal.ZERO;

    @NotNull(message = "Planned expense is required")
    @Column(name = "planned_expense", precision = 14, scale = 2)
    @Builder.Default
    private BigDecimal plannedExpense = BigDecimal.ZERO;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
