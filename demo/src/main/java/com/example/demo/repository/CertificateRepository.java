package com.example.demo.repository;

import com.example.demo.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findBySha256Hash(String sha256Hash);
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    List<Certificate> findByInstitutionId(Long institutionId);
    List<Certificate> findByStudentNameContainingIgnoreCase(String studentName);
}
