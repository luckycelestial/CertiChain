package com.example.demo.repository;

import com.example.demo.entity.VendorBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorBillRepository extends JpaRepository<VendorBill, Long> {
    Optional<VendorBill> findByBillNumber(String billNumber);
    List<VendorBill> findByVendorId(Long vendorId);
    List<VendorBill> findByStatus(VendorBill.BillStatus status);
}
