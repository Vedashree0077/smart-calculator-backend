package com.smartcalculator.backend.service;

import com.smartcalculator.backend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    @Autowired
    private HistoryService historyService;

    public String calculate(String expression, String username) {
        try {
            String originalExpression = expression;

            double value = evaluate(expression);

            // Handle invalid numeric results
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new RuntimeException("Invalid Expression");
            }

            String result;
            if (value == (long) value) {
                result = String.valueOf((long) value);
            } else {
                result = String.valueOf(value);
            }

            historyService.saveHistory(
                    username,
                    originalExpression,
                    result
            );

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Invalid Expression");
        }
    }

    private double evaluate(String expr) {
        expr = expr.replaceAll("\\s+", "");

        // =====================================
        // PERCENTAGE LOGIC
        // =====================================

        // 8% -> 0.08
        if (expr.matches("^\\d+(\\.\\d+)?%$")) {
            double number = Double.parseDouble(
                    expr.substring(0, expr.length() - 1)
            );
            return number / 100.0;
        }

        // 8%8 -> 0.64
        if (expr.matches("^\\d+(\\.\\d+)?%\\d+(\\.\\d+)?$")) {
            String[] parts = expr.split("%");
            double a = Double.parseDouble(parts[0]);
            double b = Double.parseDouble(parts[1]);
            return (a / 100.0) * b;
        }

        // 200+10% -> 220
        // 100-10% -> 90
        if (expr.matches("^\\d+(\\.\\d+)?[+-]\\d+(\\.\\d+)?%$")) {
            int opIndex = -1;
            char operator = '+';

            for (int i = 1; i < expr.length(); i++) {
                char ch = expr.charAt(i);
                if (ch == '+' || ch == '-') {
                    opIndex = i;
                    operator = ch;
                    break;
                }
            }

            double base = Double.parseDouble(expr.substring(0, opIndex));
            double percent = Double.parseDouble(
                    expr.substring(opIndex + 1, expr.length() - 1)
            );

            double percentageValue = base * percent / 100.0;

            if (operator == '+') {
                return base + percentageValue;
            } else {
                return base - percentageValue;
            }
        }

        // Convert remaining percentages
        expr = expr.replaceAll("(\\d+(?:\\.\\d+)?)%", "($1/100)");

        // =====================================
        // IMPLICIT MULTIPLICATION
        // =====================================

        // 8(2) -> 8*(2)
        expr = expr.replaceAll("(\\d)\\(", "$1*(");

        // (2)8 -> (2)*8
        expr = expr.replaceAll("\\)(\\d)", ")*$1");

        // (2)(3) -> (2)*(3)
        expr = expr.replaceAll("\\)\\(", ")*(");

        // 2sqrt(25) -> 2*sqrt(25)
        // 2sin(0) -> 2*sin(0)
        expr = expr.replaceAll(
                "(\\d)(sin|cos|tan|log|ln|sqrt)\\(",
                "$1*$2("
        );

        // =====================================
        // FUNCTIONS INSIDE EXPRESSIONS
        // =====================================

        // sqrt(...)
        while (expr.contains("sqrt(")) {
            int start = expr.indexOf("sqrt(");
            int open = start + 4; // position of '('
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            if (value < 0) {
                throw new RuntimeException("Invalid Expression");
            }

            double result = Math.sqrt(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // sin(...)
        while (expr.contains("sin(")) {
            int start = expr.indexOf("sin(");
            int open = start + 3;
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            double result = Math.sin(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // cos(...)
        while (expr.contains("cos(")) {
            int start = expr.indexOf("cos(");
            int open = start + 3;
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            double result = Math.cos(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // tan(...)
        while (expr.contains("tan(")) {
            int start = expr.indexOf("tan(");
            int open = start + 3;
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            double result = Math.tan(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // log(...)
        while (expr.contains("log(")) {
            int start = expr.indexOf("log(");
            int open = start + 3;
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            if (value <= 0) {
                throw new RuntimeException("Invalid Expression");
            }

            double result = Math.log10(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // ln(...)
        while (expr.contains("ln(")) {
            int start = expr.indexOf("ln(");
            int open = start + 2;
            int close = findClosingParenthesis(expr, open);

            String inside = expr.substring(open + 1, close);
            double value = evaluate(inside);

            if (value <= 0) {
                throw new RuntimeException("Invalid Expression");
            }

            double result = Math.log(value);

            expr = expr.substring(0, start)
                    + formatNumber(result)
                    + expr.substring(close + 1);
        }

        // =====================================
        // BASIC ARITHMETIC
        // =====================================

        double result = new com.smartcalculator.backend.util.ExpressionParser()
                .evaluate(expr);

        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new RuntimeException("Invalid Expression");
        }

        return result;
    }

    // Find matching closing parenthesis
    private int findClosingParenthesis(String expr, int openPos) {
        int count = 0;

        for (int i = openPos; i < expr.length(); i++) {
            if (expr.charAt(i) == '(') count++;
            if (expr.charAt(i) == ')') count--;

            if (count == 0) {
                return i;
            }
        }

        throw new RuntimeException("Missing closing parenthesis");
    }

    // Format double to clean string
    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}