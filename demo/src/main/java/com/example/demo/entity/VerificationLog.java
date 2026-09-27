package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_hash", nullable = false, length = 64)
    private String certificateHash;

    @Column(name = "verifier_agency")
    private String verifierAgency;

    @Column(name = "verifier_ip")
    private String verifierIp;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    @Column(name = "fee_charged", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal feeCharged = BigDecimal.ZERO;

    @Column(name = "verification_time", nullable = false)
    private LocalDateTime verificationTime;

    @PrePersist
    protected void onCreate() {
        this.verificationTime = LocalDateTime.now();
    }
}
