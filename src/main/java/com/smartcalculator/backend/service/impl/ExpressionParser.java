package com.smartcalculator.backend.service.impl;

public class ExpressionParser {

    private final String str;
    private int pos = -1;
    private int ch;

    public ExpressionParser(String str) {
        this.str = str;
    }

    public double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < str.length()) {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }
        return x;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();

        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();

        while (true) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();

        while (true) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else if (eat('%')) x %= parseFactor();
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();

        double x;
        int startPos = this.pos;

        if (eat('(')) {
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = Double.parseDouble(str.substring(startPos, this.pos));
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        return x;
    }
}