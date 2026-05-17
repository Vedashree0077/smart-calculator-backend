package com.smartcalculator.backend.util;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder numberBuffer = new StringBuilder();

        for (char ch : expression.toCharArray()) {

            if (Character.isDigit(ch) || ch == '.') {
                numberBuffer.append(ch);
            } else {
                if (numberBuffer.length() > 0) {
                    tokens.add(numberBuffer.toString());
                    numberBuffer.setLength(0);
                }

                if (!Character.isWhitespace(ch)) {
                    tokens.add(String.valueOf(ch));
                }
            }
        }

        if (numberBuffer.length() > 0) {
            tokens.add(numberBuffer.toString());
        }

        return tokens;
    }
}