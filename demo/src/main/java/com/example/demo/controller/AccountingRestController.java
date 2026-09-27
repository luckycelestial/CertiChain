package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounting")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AccountingRestController {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final VendorBillRepository vendorBillRepository;
    private final PaymentRepository paymentRepository;
    private final BillingService billingService;

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }

    @GetMapping("/journal-entries")
    public ResponseEntity<List<JournalEntry>> getJournalEntries() {
        return ResponseEntity.ok(journalEntryRepository.findByOrderByEntryDateDesc());
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<CustomerInvoice>> getInvoices() {
        return ResponseEntity.ok(customerInvoiceRepository.findAll());
    }

    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<Payment> payInvoice(@PathVariable Long id) {
        Payment payment = billingService.payCustomerInvoice(id);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/bills")
    public ResponseEntity<List<VendorBill>> getBills() {
        return ResponseEntity.ok(vendorBillRepository.findAll());
    }

    @PostMapping("/bills/{id}/pay")
    public ResponseEntity<Payment> payBill(@PathVariable Long id) {
        Payment payment = billingService.payVendorBill(id);
        return ResponseEntity.ok(payment);
    }
}
