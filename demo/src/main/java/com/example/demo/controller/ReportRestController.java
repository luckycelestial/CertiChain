package com.example.demo.controller;

import com.example.demo.dto.FinancialReportDto;
import com.example.demo.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportRestController {

    private final ReportService reportService;

    @GetMapping("/financial")
    public ResponseEntity<FinancialReportDto> getFinancialReport() {
        return ResponseEntity.ok(reportService.generateFinancialReport());
    }
}
