package com.smartcalculator.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_history")
@Getter
@Setter
public class CalculationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String expression;

    private String result;

    private LocalDateTime createdAt = LocalDateTime.now();
}