package com.example.demo.controller;

import com.example.demo.dto.CertificateIssueRequest;
import com.example.demo.dto.CertificateVerifyResponse;
import com.example.demo.entity.Certificate;
import com.example.demo.repository.*;
import com.example.demo.service.BillingService;
import com.example.demo.service.CertificateService;
import com.example.demo.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebViewController {

    private final CertificateService certificateService;
    private final InstitutionRepository institutionRepository;
    private final CertificateRepository certificateRepository;
    private final VerificationLogRepository verificationLogRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final VendorBillRepository vendorBillRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final AccountRepository accountRepository;
    private final BillingService billingService;
    private final ReportService reportService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalCerts", certificateRepository.count());
        model.addAttribute("totalVerifications", verificationLogRepository.count());
        model.addAttribute("totalInstitutions", institutionRepository.count());
        model.addAttribute("recentCerts", certificateRepository.findAll());
        model.addAttribute("recentLogs", verificationLogRepository.findTop10ByOrderByVerificationTimeDesc());
        model.addAttribute("financials", reportService.generateFinancialReport());
        return "index";
    }

    @GetMapping("/verify")
    public String verifyPage(@RequestParam(required = false) String hash, Model model, HttpServletRequest request) {
        if (hash != null && !hash.trim().isEmpty()) {
            CertificateVerifyResponse result = certificateService.verifyByHash(hash.trim(), "Public Web Portal", request.getRemoteAddr());
            model.addAttribute("result", result);
            model.addAttribute("searchedHash", hash.trim());
        }
        return "verify";
    }

    @GetMapping("/certificates")
    public String certificatesPage(Model model) {
        model.addAttribute("certificates", certificateRepository.findAll());
        model.addAttribute("institutions", institutionRepository.findAll());
        if (!model.containsAttribute("issueRequest")) {
            model.addAttribute("issueRequest", new CertificateIssueRequest());
        }
        return "certificates";
    }

    @PostMapping("/certificates/issue")
    public String issueCertificate(@Valid @ModelAttribute("issueRequest") CertificateIssueRequest request,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("certificates", certificateRepository.findAll());
            model.addAttribute("institutions", institutionRepository.findAll());
            return "certificates";
        }
        try {
            Certificate issued = certificateService.issueCertificate(request);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Certificate issued successfully! Cryptographic SHA-256 Hash: " + issued.getSha256Hash());
            return "redirect:/certificates";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error issuing certificate: " + e.getMessage());
            return "redirect:/certificates";
        }
    }

    @GetMapping("/accounting")
    public String accountingPage(Model model) {
        model.addAttribute("invoices", customerInvoiceRepository.findAll());
        model.addAttribute("bills", vendorBillRepository.findAll());
        model.addAttribute("journalEntries", journalEntryRepository.findByOrderByEntryDateDesc());
        model.addAttribute("accounts", accountRepository.findAll());
        return "accounting";
    }

    @PostMapping("/accounting/invoices/{id}/pay")
    public String payInvoice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            billingService.payCustomerInvoice(id);
            redirectAttributes.addFlashAttribute("successMessage", "Invoice marked as paid and bank ledger updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Payment failed: " + e.getMessage());
        }
        return "redirect:/accounting";
    }

    @PostMapping("/accounting/bills/{id}/pay")
    public String payBill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            billingService.payVendorBill(id);
            redirectAttributes.addFlashAttribute("successMessage", "Vendor bill marked as paid and bank ledger debited!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bill payment failed: " + e.getMessage());
        }
        return "redirect:/accounting";
    }

    @GetMapping("/reports")
    public String reportsPage(Model model) {
        model.addAttribute("report", reportService.generateFinancialReport());
        return "reports";
    }
}
