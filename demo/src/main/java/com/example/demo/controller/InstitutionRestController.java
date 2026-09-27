package com.example.demo.controller;

import com.example.demo.entity.Institution;
import com.example.demo.repository.InstitutionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InstitutionRestController {

    private final InstitutionRepository institutionRepository;

    @PostMapping
    public ResponseEntity<Institution> registerInstitution(@Valid @RequestBody Institution institution) {
        Institution saved = institutionRepository.save(institution);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Institution>> listInstitutions() {
        return ResponseEntity.ok(institutionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitution(@PathVariable Long id) {
        return institutionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
