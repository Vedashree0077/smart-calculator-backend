package com.smartcalculator.backend.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Stack;

@Component
public class ExpressionParser {

    public double evaluate(String expression) {

        List<String> tokens = Tokenizer.tokenize(expression);

        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (int i = 0; i < tokens.size(); i++) {

            String token = tokens.get(i);

            // Handle unary minus:
            // -5
            // (-5)
            // 5*-5
            // -(5+2)
            if (token.equals("-")) {
                if (i == 0 ||
                        tokens.get(i - 1).equals("(") ||
                        OperatorPrecedence.isOperator(tokens.get(i - 1))) {

                    // Case 1: -5
                    if (i + 1 < tokens.size() && isNumber(tokens.get(i + 1))) {
                        values.push(-Double.parseDouble(tokens.get(i + 1)));
                        i++;
                        continue;
                    }

                    // Case 2: -(5+2)
                    if (i + 1 < tokens.size() &&
                            tokens.get(i + 1).equals("(")) {
                        values.push(-1.0);
                        operators.push("*");
                        continue;
                    }
                }
            }

            if (isNumber(token)) {
                values.push(Double.parseDouble(token));

            } else if (token.equals("(")) {
                operators.push(token);

            } else if (token.equals(")")) {
                while (!operators.isEmpty() &&
                        !operators.peek().equals("(")) {
                    applyOperation(values, operators.pop());
                }

                if (!operators.isEmpty() &&
                        operators.peek().equals("(")) {
                    operators.pop();
                }

            } else if (OperatorPrecedence.isOperator(token)) {
                while (!operators.isEmpty() &&
                        !operators.peek().equals("(") &&
                        OperatorPrecedence.getPrecedence(operators.peek()) >=
                                OperatorPrecedence.getPrecedence(token)) {

                    applyOperation(values, operators.pop());
                }

                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            applyOperation(values, operators.pop());
        }

        if (values.isEmpty()) {
            throw new RuntimeException("Invalid Expression");
        }

        return values.pop();
    }

    private void applyOperation(Stack<Double> values, String operator) {

        if (values.size() < 2) {
            throw new RuntimeException("Invalid Expression");
        }

        double b = values.pop();
        double a = values.pop();

        switch (operator) {
            case "+" -> values.push(a + b);
            case "-" -> values.push(a - b);
            case "*" -> values.push(a * b);
            case "/" -> {
                if (b == 0) {
                    throw new ArithmeticException("Divide by zero");
                }
                values.push(a / b);
            }
            default -> throw new RuntimeException("Unknown operator");
        }
    }

    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}