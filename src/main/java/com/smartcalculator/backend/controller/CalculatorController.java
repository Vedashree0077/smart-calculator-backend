package com.smartcalculator.backend.controller;

import com.smartcalculator.backend.dto.request.CalculatorRequest;
import com.smartcalculator.backend.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "http://localhost:5173")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    @PostMapping("/calculate")
    public String calculate(
            @RequestBody CalculatorRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();

        return calculatorService.calculate(
                request.getExpression(),
                username
        );
    }
}