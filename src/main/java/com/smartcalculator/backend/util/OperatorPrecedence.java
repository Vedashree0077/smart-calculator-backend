package com.smartcalculator.backend.util;

import java.util.HashMap;
import java.util.Map;

public class OperatorPrecedence {

    private static final Map<String, Integer> precedenceMap = new HashMap<>();

    static {
        precedenceMap.put("+", 1);
        precedenceMap.put("-", 1);
        precedenceMap.put("*", 2);
        precedenceMap.put("/", 2);
    }

    public static int getPrecedence(String operator) {
        return precedenceMap.getOrDefault(operator, -1);
    }

    public static boolean isOperator(String token) {
        return precedenceMap.containsKey(token);
    }
}