package com.smartcalculator.backend.controller;

import com.smartcalculator.backend.entity.CalculationHistory;
import com.smartcalculator.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "http://localhost:5173")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    // Get all history for logged-in user
    @GetMapping
    public ResponseEntity<List<CalculationHistory>> getHistory(Authentication authentication) {
        String username = authentication.getName();
        List<CalculationHistory> history = historyService.getHistory(username);
        return ResponseEntity.ok(history);
    }

    // Clear all history for logged-in user
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearHistory(Authentication authentication) {
        try {
            String username = authentication.getName();
            historyService.clearHistory(username);
            return ResponseEntity.ok("History cleared successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Failed to clear history: " + e.getMessage());
        }
    }
}