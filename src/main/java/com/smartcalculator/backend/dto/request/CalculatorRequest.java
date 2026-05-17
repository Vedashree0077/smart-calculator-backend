package com.smartcalculator.backend.dto.request;

public class CalculatorRequest {

    private String expression;

    public CalculatorRequest() {
    }

    public CalculatorRequest(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}