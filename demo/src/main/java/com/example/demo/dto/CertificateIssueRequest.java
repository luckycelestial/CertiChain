package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateIssueRequest {

    @NotBlank(message = "Certificate number is required")
    private String certificateNumber;

    @NotBlank(message = "Student name is required")
    private String studentName;

    private String studentEmail;

    @NotBlank(message = "Degree title is required")
    private String degree;

    private String major;

    private Double cgpa;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Institution ID is required")
    private Long institutionId;
}
