package com.example.demo.controller;

import com.example.demo.dto.CertificateIssueRequest;
import com.example.demo.dto.CertificateVerifyResponse;
import com.example.demo.entity.Certificate;
import com.example.demo.service.CertificateService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CertificateRestController {

    private final CertificateService certificateService;

    /**
     * Issues a new academic credential with cryptographic SHA-256 hash.
     * POST /api/certificates
     */
    @PostMapping
    public ResponseEntity<Certificate> issueCertificate(@Valid @RequestBody CertificateIssueRequest request) {
        Certificate created = certificateService.issueCertificate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Verifies a certificate using its cryptographic SHA-256 hash.
     * GET /api/certificates/verify/{hash}
     */
    @GetMapping("/verify/{hash}")
    public ResponseEntity<CertificateVerifyResponse> verifyCertificate(
            @PathVariable String hash,
            @RequestParam(required = false, defaultValue = "External REST API") String agency,
            HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        CertificateVerifyResponse response = certificateService.verifyByHash(hash, agency, ip);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all issued certificates.
     * GET /api/certificates
     */
    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    /**
     * Retrieves certificate by ID.
     * GET /api/certificates/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getCertificateById(@PathVariable Long id) {
        return certificateService.getCertificateById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
