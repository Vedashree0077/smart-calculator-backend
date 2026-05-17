package com.smartcalculator.backend.repository;

import com.smartcalculator.backend.entity.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CalculationHistoryRepository
        extends JpaRepository<CalculationHistory, Long> {

    List<CalculationHistory> findByUsernameOrderByIdDesc(String username);

    @Transactional
    long deleteByUsername(String username);
}