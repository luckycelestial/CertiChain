package com.example.demo.service;

import com.example.demo.dto.FinancialReportDto;
import com.example.demo.entity.Account;
import com.example.demo.entity.Budget;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;

    public FinancialReportDto generateFinancialReport() {
        List<Account> allAccounts = accountRepository.findAll();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;
        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;

        Map<String, BigDecimal> revenueBreakdown = new HashMap<>();
        Map<String, BigDecimal> expenseBreakdown = new HashMap<>();
        Map<String, BigDecimal> assetBreakdown = new HashMap<>();
        Map<String, BigDecimal> liabilityBreakdown = new HashMap<>();

        for (Account acc : allAccounts) {
            BigDecimal bal = acc.getBalance() != null ? acc.getBalance().abs() : BigDecimal.ZERO;
            switch (acc.getType()) {
                case INCOME -> {
                    totalRevenue = totalRevenue.add(bal);
                    revenueBreakdown.put(acc.getName(), bal);
                }
                case EXPENSE -> {
                    totalExpenses = totalExpenses.add(bal);
                    expenseBreakdown.put(acc.getName(), bal);
                }
                case ASSET -> {
                    totalAssets = totalAssets.add(bal);
                    assetBreakdown.put(acc.getName(), bal);
                }
                case LIABILITY -> {
                    totalLiabilities = totalLiabilities.add(bal);
                    liabilityBreakdown.put(acc.getName(), bal);
                }
                default -> {}
            }
        }

        BigDecimal netProfit = totalRevenue.subtract(totalExpenses);
        BigDecimal totalEquity = totalAssets.subtract(totalLiabilities);

        // Budget data
        BigDecimal plannedRev = BigDecimal.ZERO;
        BigDecimal plannedExp = BigDecimal.ZERO;
        List<Budget> budgets = budgetRepository.findAll();
        for (Budget b : budgets) {
            plannedRev = plannedRev.add(b.getPlannedRevenue() != null ? b.getPlannedRevenue() : BigDecimal.ZERO);
            plannedExp = plannedExp.add(b.getPlannedExpense() != null ? b.getPlannedExpense() : BigDecimal.ZERO);
        }

        return FinancialReportDto.builder()
                .totalRevenue(totalRevenue)
                .totalExpenses(totalExpenses)
                .netProfit(netProfit)
                .totalAssets(totalAssets)
                .totalLiabilities(totalLiabilities)
                .totalEquity(totalEquity)
                .revenueBreakdown(revenueBreakdown)
                .expenseBreakdown(expenseBreakdown)
                .assetBreakdown(assetBreakdown)
                .liabilityBreakdown(liabilityBreakdown)
                .plannedBudgetRevenue(plannedRev)
                .plannedBudgetExpense(plannedExp)
                .actualBudgetRevenue(totalRevenue)
                .actualBudgetExpense(totalExpenses)
                .budgetVariance(totalRevenue.subtract(plannedRev).subtract(totalExpenses.subtract(plannedExp)))
                .build();
    }
}
