package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final AccountRepository accountRepository;
    private final JournalRepository journalRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AnalyticAccountRepository analyticAccountRepository;

    /**
     * Records a balanced double-entry journal entry.
     * Enforces Debit = Credit accounting rule.
     */
    @Transactional
    public JournalEntry createDoubleEntry(String journalCode,
                                          String reference,
                                          String debitAccountCode,
                                          String creditAccountCode,
                                          BigDecimal amount,
                                          String label,
                                          String analyticAccountCode) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }

        Journal journal = journalRepository.findByCode(journalCode)
                .orElseThrow(() -> new IllegalArgumentException("Journal not found: " + journalCode));

        Account debitAccount = accountRepository.findByCode(debitAccountCode)
                .orElseThrow(() -> new IllegalArgumentException("Debit account not found: " + debitAccountCode));

        Account creditAccount = accountRepository.findByCode(creditAccountCode)
                .orElseThrow(() -> new IllegalArgumentException("Credit account not found: " + creditAccountCode));

        AnalyticAccount analyticAccount = null;
        if (analyticAccountCode != null) {
            analyticAccount = analyticAccountRepository.findByCode(analyticAccountCode).orElse(null);
        }

        String entryNum = "JE-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        JournalEntry entry = JournalEntry.builder()
                .entryNumber(entryNum)
                .entryDate(LocalDate.now())
                .reference(reference)
                .journal(journal)
                .status(JournalEntry.EntryStatus.POSTED)
                .totalDebit(amount)
                .totalCredit(amount)
                .build();

        // 1. Debit Item
        JournalItem debitItem = JournalItem.builder()
                .account(debitAccount)
                .label(label + " [Debit]")
                .debit(amount)
                .credit(BigDecimal.ZERO)
                .analyticAccount(analyticAccount)
                .build();
        entry.addItem(debitItem);

        // 2. Credit Item
        JournalItem creditItem = JournalItem.builder()
                .account(creditAccount)
                .label(label + " [Credit]")
                .debit(BigDecimal.ZERO)
                .credit(amount)
                .analyticAccount(analyticAccount)
                .build();
        entry.addItem(creditItem);

        // Update Account Balances
        updateAccountBalance(debitAccount, amount, true);
        updateAccountBalance(creditAccount, amount, false);

        return journalEntryRepository.save(entry);
    }

    private void updateAccountBalance(Account account, BigDecimal amount, boolean isDebit) {
        // Asset/Expense: Debit increases (+), Credit decreases (-)
        // Liability/Equity/Income: Credit increases (+), Debit decreases (-)
        BigDecimal current = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        boolean increasesWithDebit = account.getType() == Account.AccountType.ASSET || account.getType() == Account.AccountType.EXPENSE;

        if (increasesWithDebit) {
            account.setBalance(isDebit ? current.add(amount) : current.subtract(amount));
        } else {
            account.setBalance(!isDebit ? current.add(amount) : current.subtract(amount));
        }
        accountRepository.save(account);
    }

    /**
     * Records Certificate Issuance Revenue (Debit: Cash/Bank, Credit: Issuance Income)
     */
    @Transactional
    public void recordCertificateIssuanceRevenue(Certificate cert, BigDecimal fee) {
        createDoubleEntry(
                "SALES",
                "CERT-" + cert.getCertificateNumber(),
                "1010", // Bank / Cash Asset
                "4010", // Certificate Issuance Revenue
                fee,
                "Cryptographic Issuance Fee: " + cert.getStudentName(),
                "AN-OPS-01" // Analytic Unit
        );
    }

    /**
     * Records Verification API Subscription Sale (Debit: Accounts Receivable, Credit: API Revenue)
     */
    @Transactional
    public void recordApiSalesInvoice(CustomerInvoice invoice) {
        createDoubleEntry(
                "SALES",
                invoice.getInvoiceNumber(),
                "1020", // Accounts Receivable
                "4020", // Verification API Revenue
                invoice.getAmount(),
                "API Verification Package - " + invoice.getCustomer().getName(),
                "AN-OPS-01"
        );
    }

    /**
     * Records Payment from Agency (Debit: Bank, Credit: Accounts Receivable)
     */
    @Transactional
    public void recordCustomerPayment(Payment payment) {
        createDoubleEntry(
                "BANK",
                payment.getPaymentNumber(),
                "1010", // Bank
                "1020", // Accounts Receivable
                payment.getAmount(),
                "Payment Received: " + payment.getContact().getName(),
                "AN-OPS-01"
        );
    }

    /**
     * Records Cloud Hosting Server Bill (Debit: Cloud Infrastructure Expense, Credit: Accounts Payable)
     */
    @Transactional
    public void recordVendorBill(VendorBill bill) {
        createDoubleEntry(
                "PURCHASE",
                bill.getBillNumber(),
                "5010", // Cloud Infrastructure Expense
                "2010", // Cloud Vendor Payables
                bill.getAmount(),
                "Cloud Server Hosting - " + bill.getVendor().getName(),
                "AN-OPS-01"
        );
    }

    /**
     * Records Payment to Cloud Vendor (Debit: Accounts Payable, Credit: Bank)
     */
    @Transactional
    public void recordVendorPayment(Payment payment) {
        createDoubleEntry(
                "BANK",
                payment.getPaymentNumber(),
                "2010", // Accounts Payable
                "1010", // Bank
                payment.getAmount(),
                "Vendor Payment to " + payment.getContact().getName(),
                "AN-OPS-01"
        );
    }
}
