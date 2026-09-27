package com.example.demo.repository;

import com.example.demo.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long> {
    Optional<CustomerInvoice> findByInvoiceNumber(String invoiceNumber);
    List<CustomerInvoice> findByCustomerId(Long customerId);
    List<CustomerInvoice> findByStatus(CustomerInvoice.InvoiceStatus status);
}
