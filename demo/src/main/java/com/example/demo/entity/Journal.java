package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "journals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Journal code is required")
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @NotBlank(message = "Journal name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private JournalType type;

    public enum JournalType {
        SALES,      // API Query sales
        PURCHASE,   // Server hosting / cloud vendor bills
        BANK,       // Bank transfers
        CASH        // Cash operations
    }
}
