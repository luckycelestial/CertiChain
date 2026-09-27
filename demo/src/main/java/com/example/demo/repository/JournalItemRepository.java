package com.example.demo.repository;

import com.example.demo.entity.JournalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JournalItemRepository extends JpaRepository<JournalItem, Long> {

    List<JournalItem> findByAccountId(Long accountId);

    @Query("SELECT SUM(j.debit) FROM JournalItem j WHERE j.account.id = :accountId")
    BigDecimal sumDebitByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT SUM(j.credit) FROM JournalItem j WHERE j.account.id = :accountId")
    BigDecimal sumCreditByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT SUM(j.credit) - SUM(j.debit) FROM JournalItem j WHERE j.account.type = 'INCOME'")
    BigDecimal sumTotalIncome();

    @Query("SELECT SUM(j.debit) - SUM(j.credit) FROM JournalItem j WHERE j.account.type = 'EXPENSE'")
    BigDecimal sumTotalExpenses();
}
