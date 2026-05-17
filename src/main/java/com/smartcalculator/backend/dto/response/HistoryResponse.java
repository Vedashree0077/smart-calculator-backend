package com.smartcalculator.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HistoryResponse {

    private String expression;
    private String result;
    private LocalDateTime timestamp;

}