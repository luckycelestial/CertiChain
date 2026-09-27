package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialReportDto {
    private BigDecimal totalRevenue;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;

    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal totalEquity;

    private Map<String, BigDecimal> revenueBreakdown;
    private Map<String, BigDecimal> expenseBreakdown;
    private Map<String, BigDecimal> assetBreakdown;
    private Map<String, BigDecimal> liabilityBreakdown;

    // Budget Comparison
    private BigDecimal plannedBudgetRevenue;
    private BigDecimal plannedBudgetExpense;
    private BigDecimal actualBudgetRevenue;
    private BigDecimal actualBudgetExpense;
    private BigDecimal budgetVariance;
}
