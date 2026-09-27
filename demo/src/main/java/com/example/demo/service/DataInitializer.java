package com.example.demo.service;

import com.example.demo.dto.CertificateIssueRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final InstitutionRepository institutionRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final AccountRepository accountRepository;
    private final JournalRepository journalRepository;
    private final AnalyticAccountRepository analyticAccountRepository;
    private final BudgetRepository budgetRepository;
    private final CertificateRepository certificateRepository;
    private final CertificateService certificateService;
    private final BillingService billingService;

    @Override
    public void run(String... args) throws Exception {
        if (institutionRepository.count() > 0) {
            log.info("Database already seeded with initial data.");
            return;
        }

        log.info("Seeding CertiChain Master Data & Chart of Accounts...");

        // 1. Seed Chart of Accounts
        Account bankAcc = accountRepository.save(Account.builder()
                .code("1010").name("Bank & Cash Assets").type(Account.AccountType.ASSET).balance(new BigDecimal("25000.00")).build());

        Account arAcc = accountRepository.save(Account.builder()
                .code("1020").name("Accounts Receivable (Client Invoices)").type(Account.AccountType.ASSET).balance(BigDecimal.ZERO).build());

        Account ipAcc = accountRepository.save(Account.builder()
                .code("1030").name("Cryptographic IP Infrastructure").type(Account.AccountType.ASSET).balance(new BigDecimal("50000.00")).build());

        Account apAcc = accountRepository.save(Account.builder()
                .code("2010").name("Cloud Vendor Payables").type(Account.AccountType.LIABILITY).balance(BigDecimal.ZERO).build());

        Account certRevAcc = accountRepository.save(Account.builder()
                .code("4010").name("Certificate Cryptographic Issuance Revenue").type(Account.AccountType.INCOME).balance(BigDecimal.ZERO).build());

        Account apiRevAcc = accountRepository.save(Account.builder()
                .code("4020").name("Verification API Service Revenue").type(Account.AccountType.INCOME).balance(BigDecimal.ZERO).build());

        Account hostExpAcc = accountRepository.save(Account.builder()
                .code("5010").name("Cloud Infrastructure Expenses").type(Account.AccountType.EXPENSE).balance(BigDecimal.ZERO).build());

        // 2. Seed Journals
        journalRepository.save(Journal.builder().code("SALES").name("Sales Journal (API Queries & Certs)").type(Journal.JournalType.SALES).build());
        journalRepository.save(Journal.builder().code("PURCHASE").name("Purchase Journal (Server Hosting)").type(Journal.JournalType.PURCHASE).build());
        journalRepository.save(Journal.builder().code("BANK").name("Bank & Cash Journal").type(Journal.JournalType.BANK).build());

        // 3. Seed Analytic Account & Operational Budget
        AnalyticAccount analyticOps = analyticAccountRepository.save(AnalyticAccount.builder()
                .code("AN-OPS-01")
                .name("Enterprise Verification API Sector")
                .description("Operations unit for institutional credential verification APIs")
                .build());

        budgetRepository.save(Budget.builder()
                .name("FY2026 Verification Unit Annual Budget")
                .analyticAccount(analyticOps)
                .plannedRevenue(new BigDecimal("120000.00"))
                .plannedExpense(new BigDecimal("35000.00"))
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .build());

        // 4. Seed Products
        Product certService = productRepository.save(Product.builder()
                .code("PROD-CERT-ISSUE")
                .name("Certificate Cryptographic Issuance")
                .type(Product.ProductType.SERVICE)
                .unitPrice(new BigDecimal("50.00"))
                .description("On-chain tamper-proof cryptographic SHA-256 certificate generation")
                .build());

        Product apiService = productRepository.save(Product.builder()
                .code("PROD-API-VERIFY")
                .name("Verification API Query Package (1000 Calls)")
                .type(Product.ProductType.SERVICE)
                .unitPrice(new BigDecimal("250.00"))
                .description("Enterprise REST API query package for background-check firms")
                .build());

        Product cloudHosting = productRepository.save(Product.builder()
                .code("PROD-SERVER-HOST")
                .name("Dedicated Cloud Security HSM Server")
                .type(Product.ProductType.SERVICE)
                .unitPrice(new BigDecimal("1200.00"))
                .description("Monthly secure cloud HSM server hosting")
                .build());

        // 5. Seed Contacts
        Contact university = contactRepository.save(Contact.builder()
                .name("Sri Eshwar College of Engineering")
                .type(Contact.ContactType.UNIVERSITY_CLIENT)
                .email("registrar@sece.ac.in")
                .phone("+91-422-2634000")
                .address("Coimbatore, Tamil Nadu, India")
                .build());

        Contact agency = contactRepository.save(Contact.builder()
                .name("Global Background Verification Agency")
                .type(Contact.ContactType.AGENCY_CUSTOMER)
                .email("api-billing@globalverify.com")
                .phone("+1-800-555-0199")
                .address("New York, USA")
                .build());

        Contact cloudVendor = contactRepository.save(Contact.builder()
                .name("AWS / Azure Cloud Infrastructure Services")
                .type(Contact.ContactType.CLOUD_VENDOR)
                .email("accounts@cloudhost.com")
                .phone("+1-888-280-4331")
                .address("Seattle, WA, USA")
                .build());

        // 6. Seed Accredited Institutions
        Institution inst1 = institutionRepository.save(Institution.builder()
                .name("Sri Eshwar College of Engineering")
                .code("SECE-7228")
                .email("registrar@sece.ac.in")
                .website("https://sece.ac.in")
                .accredited(true)
                .build());

        Institution inst2 = institutionRepository.save(Institution.builder()
                .name("Anna University")
                .code("AU-0001")
                .email("coe@annauniv.edu")
                .website("https://annauniv.edu")
                .accredited(true)
                .build());

        // 7. Seed Sample Certificates with SHA-256 Hashes
        certificateService.issueCertificate(CertificateIssueRequest.builder()
                .certificateNumber("CERT-2026-001")
                .studentName("Pavithran P N")
                .studentEmail("pavithran@example.com")
                .degree("Bachelor of Technology")
                .major("Artificial Intelligence and Data Science")
                .cgpa(9.45)
                .issueDate(LocalDate.of(2026, 6, 15))
                .institutionId(inst1.getId())
                .build());

        certificateService.issueCertificate(CertificateIssueRequest.builder()
                .certificateNumber("CERT-2026-002")
                .studentName("Jeeshitha M")
                .studentEmail("jeeshitha@example.com")
                .degree("Bachelor of Technology")
                .major("Artificial Intelligence and Data Science")
                .cgpa(9.20)
                .issueDate(LocalDate.of(2026, 6, 15))
                .institutionId(inst1.getId())
                .build());

        // 8. Seed Sample Transactions (Sales Order -> Invoice -> Payment & Purchase Order -> Bill -> Payment)
        SalesOrder so = billingService.createSalesOrder(agency.getId(), apiService.getId(), 2);
        CustomerInvoice inv = billingService.createInvoiceFromSalesOrder(so.getId());
        billingService.payCustomerInvoice(inv.getId());

        PurchaseOrder po = billingService.createPurchaseOrder(cloudVendor.getId(), cloudHosting.getId(), 1);
        VendorBill bill = billingService.createVendorBillFromPO(po.getId());
        billingService.payVendorBill(bill.getId());

        log.info("CertiChain initial data seeding successfully completed!");
    }
}
