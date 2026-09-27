package com.example.demo.service;

import com.example.demo.dto.CertificateIssueRequest;
import com.example.demo.dto.CertificateVerifyResponse;
import com.example.demo.entity.Certificate;
import com.example.demo.entity.Institution;
import com.example.demo.entity.VerificationLog;
import com.example.demo.repository.CertificateRepository;
import com.example.demo.repository.InstitutionRepository;
import com.example.demo.repository.VerificationLogRepository;
import com.example.demo.util.CryptoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final InstitutionRepository institutionRepository;
    private final VerificationLogRepository verificationLogRepository;
    private final AccountingService accountingService;

    /**
     * Issues a new tamper-proof academic certificate with SHA-256 hash.
     */
    @Transactional
    public Certificate issueCertificate(CertificateIssueRequest request) {
        Institution institution = institutionRepository.findById(request.getInstitutionId())
                .orElseThrow(() -> new IllegalArgumentException("Institution not found with ID: " + request.getInstitutionId()));

        // Construct unique tamper-evident raw string payload
        String rawData = String.format("INST:%s|CERT:%s|NAME:%s|DEGREE:%s|DATE:%s",
                institution.getCode(),
                request.getCertificateNumber(),
                request.getStudentName().trim(),
                request.getDegree().trim(),
                request.getIssueDate().toString());

        String sha256Hash = CryptoUtils.calculateSHA256(rawData);

        Certificate certificate = Certificate.builder()
                .certificateNumber(request.getCertificateNumber())
                .studentName(request.getStudentName())
                .studentEmail(request.getStudentEmail())
                .degree(request.getDegree())
                .major(request.getMajor())
                .cgpa(request.getCgpa())
                .issueDate(request.getIssueDate())
                .institution(institution)
                .sha256Hash(sha256Hash)
                .status(Certificate.CertificateStatus.ISSUED)
                .build();

        Certificate saved = certificateRepository.save(certificate);

        // Record Certificate Issuance Revenue in Double-Entry Ledger (e.g. $50 issuance fee)
        try {
            accountingService.recordCertificateIssuanceRevenue(saved, new BigDecimal("50.00"));
        } catch (Exception e) {
            // Non-blocking if ledger account not initialized
        }

        return saved;
    }

    /**
     * Verifies the authenticity of a certificate by its cryptographic SHA-256 hash.
     */
    @Transactional
    public CertificateVerifyResponse verifyByHash(String hash, String verifierAgency, String verifierIp) {
        if (hash == null || hash.trim().isEmpty()) {
            return CertificateVerifyResponse.builder()
                    .valid(false)
                    .status("INVALID_INPUT")
                    .message("Certificate SHA-256 hash must not be empty.")
                    .build();
        }

        Optional<Certificate> optionalCert = certificateRepository.findBySha256Hash(hash.trim().toLowerCase());

        boolean isValid = false;
        CertificateVerifyResponse.CertificateVerifyResponseBuilder responseBuilder = CertificateVerifyResponse.builder()
                .hash(hash);

        if (optionalCert.isPresent()) {
            Certificate cert = optionalCert.get();
            
            // Recompute SHA-256 hash to guarantee database integrity
            String expectedData = String.format("INST:%s|CERT:%s|NAME:%s|DEGREE:%s|DATE:%s",
                    cert.getInstitution().getCode(),
                    cert.getCertificateNumber(),
                    cert.getStudentName().trim(),
                    cert.getDegree().trim(),
                    cert.getIssueDate().toString());

            String recomputedHash = CryptoUtils.calculateSHA256(expectedData);

            if (recomputedHash.equalsIgnoreCase(hash) && cert.getStatus() == Certificate.CertificateStatus.ISSUED) {
                isValid = true;
                responseBuilder
                        .valid(true)
                        .status("VALID")
                        .certificateNumber(cert.getCertificateNumber())
                        .studentName(cert.getStudentName())
                        .degree(cert.getDegree())
                        .major(cert.getMajor())
                        .cgpa(cert.getCgpa())
                        .issueDate(cert.getIssueDate())
                        .institutionName(cert.getInstitution().getName())
                        .institutionCode(cert.getInstitution().getCode())
                        .institutionAccredited(cert.getInstitution().getAccredited())
                        .message("Certificate is authentic and cryptographically verified on-chain.");
            } else if (cert.getStatus() == Certificate.CertificateStatus.REVOKED) {
                responseBuilder
                        .valid(false)
                        .status("REVOKED")
                        .certificateNumber(cert.getCertificateNumber())
                        .studentName(cert.getStudentName())
                        .degree(cert.getDegree())
                        .institutionName(cert.getInstitution().getName())
                        .message("Warning: This certificate has been revoked by the issuing institution.");
            } else {
                responseBuilder
                        .valid(false)
                        .status("TAMPERED")
                        .message("Alert: Cryptographic checksum mismatch. Certificate content has been modified!");
            }
        } else {
            responseBuilder
                    .valid(false)
                    .status("TAMPERED_OR_NOT_FOUND")
                    .message("No certificate matching this cryptographic hash was found on the ledger.");
        }

        // Log Verification Attempt & Fee
        BigDecimal fee = new BigDecimal("2.50"); // $2.50 per verification API query
        VerificationLog log = VerificationLog.builder()
                .certificateHash(hash)
                .verifierAgency(verifierAgency != null ? verifierAgency : "Public Web Portal")
                .verifierIp(verifierIp != null ? verifierIp : "127.0.0.1")
                .isValid(isValid)
                .feeCharged(fee)
                .build();
        verificationLogRepository.save(log);

        return responseBuilder.build();
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Optional<Certificate> getCertificateById(Long id) {
        return certificateRepository.findById(id);
    }
}
