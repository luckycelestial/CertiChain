package com.example.demo.repository;

import com.example.demo.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByCode(String code);
    Optional<Journal> findByType(Journal.JournalType type);
}
