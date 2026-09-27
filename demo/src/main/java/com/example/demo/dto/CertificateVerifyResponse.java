package com.example.demo.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateVerifyResponse {
    private boolean valid;
    private String status; // "VALID", "REVOKED", "TAMPERED_OR_NOT_FOUND"
    private String hash;
    private String certificateNumber;
    private String studentName;
    private String degree;
    private String major;
    private Double cgpa;
    private LocalDate issueDate;
    private String institutionName;
    private String institutionCode;
    private boolean institutionAccredited;
    private String message;
}
