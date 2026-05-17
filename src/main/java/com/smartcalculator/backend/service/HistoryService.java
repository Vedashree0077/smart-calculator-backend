package com.smartcalculator.backend.service;

import com.smartcalculator.backend.entity.CalculationHistory;
import com.smartcalculator.backend.repository.CalculationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private CalculationHistoryRepository historyRepository;

    public List<CalculationHistory> getHistory(String username) {
        return historyRepository.findByUsernameOrderByIdDesc(username);
    }

    public void saveHistory(String username, String expression, String result) {
        CalculationHistory history = new CalculationHistory();
        history.setUsername(username);
        history.setExpression(expression);
        history.setResult(result);
        historyRepository.save(history);
    }

    @Transactional
    public void clearHistory(String username) {
        historyRepository.deleteByUsername(username);
    }
}