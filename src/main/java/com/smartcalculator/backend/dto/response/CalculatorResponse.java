package com.smartcalculator.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalculatorResponse {

    private String expression;
    private String result;

}