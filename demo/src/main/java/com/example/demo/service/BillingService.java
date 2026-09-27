package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final SalesOrderRepository salesOrderRepository;
    private final CustomerInvoiceRepository customerInvoiceRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final VendorBillRepository vendorBillRepository;
    private final PaymentRepository paymentRepository;
    private final JournalRepository journalRepository;
    private final ContactRepository contactRepository;
    private final ProductRepository productRepository;
    private final AccountingService accountingService;

    /**
     * 1. Create Sales Order for Agency API Query Package
     */
    @Transactional
    public SalesOrder createSalesOrder(Long customerId, Long productId, int quantity) {
        Contact customer = contactRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        BigDecimal total = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        String orderNum = "SO-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        SalesOrder order = SalesOrder.builder()
                .orderNumber(orderNum)
                .customer(customer)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getUnitPrice())
                .totalAmount(total)
                .orderDate(LocalDate.now())
                .status(SalesOrder.OrderStatus.CONFIRMED)
                .build();

        return salesOrderRepository.save(order);
    }

    /**
     * 2. Convert Sales Order to Customer Invoice
     */
    @Transactional
    public CustomerInvoice createInvoiceFromSalesOrder(Long salesOrderId) {
        SalesOrder order = salesOrderRepository.findById(salesOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Sales order not found"));

        String invNum = "INV-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        CustomerInvoice invoice = CustomerInvoice.builder()
                .invoiceNumber(invNum)
                .customer(order.getCustomer())
                .salesOrder(order)
                .amount(order.getTotalAmount())
                .invoiceDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .status(CustomerInvoice.InvoiceStatus.POSTED)
                .build();

        order.setStatus(SalesOrder.OrderStatus.INVOICED);
        salesOrderRepository.save(order);

        CustomerInvoice savedInvoice = customerInvoiceRepository.save(invoice);
        
        // Auto double-entry journal entry
        accountingService.recordApiSalesInvoice(savedInvoice);

        return savedInvoice;
    }

    /**
     * 3. Register Customer Bank Payment for Invoice
     */
    @Transactional
    public Payment payCustomerInvoice(Long invoiceId) {
        CustomerInvoice invoice = customerInvoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));

        Journal bankJournal = journalRepository.findByCode("BANK")
                .orElseThrow(() -> new IllegalArgumentException("Bank Journal not configured"));

        String payNum = "PAY-REC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Payment payment = Payment.builder()
                .paymentNumber(payNum)
                .paymentType(Payment.PaymentType.INBOUND_CUSTOMER_RECEIPT)
                .contact(invoice.getCustomer())
                .amount(invoice.getAmount())
                .paymentDate(LocalDate.now())
                .journal(bankJournal)
                .reference(invoice.getInvoiceNumber())
                .build();

        invoice.setStatus(CustomerInvoice.InvoiceStatus.PAID);
        invoice.setPaymentDate(LocalDate.now());
        customerInvoiceRepository.save(invoice);

        Payment savedPayment = paymentRepository.save(payment);

        // Auto double-entry journal entry
        accountingService.recordCustomerPayment(savedPayment);

        return savedPayment;
    }

    /**
     * 4. Create Purchase Order for Cloud Server Infrastructure
     */
    @Transactional
    public PurchaseOrder createPurchaseOrder(Long vendorId, Long productId, int quantity) {
        Contact vendor = contactRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("Vendor not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        BigDecimal total = product.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
        String poNum = "PO-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        PurchaseOrder po = PurchaseOrder.builder()
                .poNumber(poNum)
                .vendor(vendor)
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getUnitPrice())
                .totalAmount(total)
                .orderDate(LocalDate.now())
                .status(PurchaseOrder.POStatus.CONFIRMED)
                .build();

        return purchaseOrderRepository.save(po);
    }

    /**
     * 5. Convert Purchase Order to Vendor Bill
     */
    @Transactional
    public VendorBill createVendorBillFromPO(Long purchaseOrderId) {
        PurchaseOrder po = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase order not found"));

        String billNum = "BILL-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        VendorBill bill = VendorBill.builder()
                .billNumber(billNum)
                .vendor(po.getVendor())
                .purchaseOrder(po)
                .amount(po.getTotalAmount())
                .billDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .status(VendorBill.BillStatus.POSTED)
                .build();

        po.setStatus(PurchaseOrder.POStatus.BILLED);
        purchaseOrderRepository.save(po);

        VendorBill savedBill = vendorBillRepository.save(bill);

        // Auto double-entry journal entry
        accountingService.recordVendorBill(savedBill);

        return savedBill;
    }

    /**
     * 6. Pay Vendor Bill via Bank
     */
    @Transactional
    public Payment payVendorBill(Long billId) {
        VendorBill bill = vendorBillRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found"));

        Journal bankJournal = journalRepository.findByCode("BANK")
                .orElseThrow(() -> new IllegalArgumentException("Bank Journal not configured"));

        String payNum = "PAY-OUT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        Payment payment = Payment.builder()
                .paymentNumber(payNum)
                .paymentType(Payment.PaymentType.OUTBOUND_VENDOR_PAYMENT)
                .contact(bill.getVendor())
                .amount(bill.getAmount())
                .paymentDate(LocalDate.now())
                .journal(bankJournal)
                .reference(bill.getBillNumber())
                .build();

        bill.setStatus(VendorBill.BillStatus.PAID);
        bill.setPaymentDate(LocalDate.now());
        vendorBillRepository.save(bill);

        Payment savedPayment = paymentRepository.save(payment);

        // Auto double-entry journal entry
        accountingService.recordVendorPayment(savedPayment);

        return savedPayment;
    }
}
