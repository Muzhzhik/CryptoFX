package com.example.cryptofx.utils;

public enum Action {
    ENCRYPT, DECRYPT, BRUTEFORCE, ANALYZE;

    public static Action getAction(int actionIndex) {
        Action action = null;
        if (actionIndex >= ENCRYPT.ordinal() && actionIndex <= ANALYZE.ordinal()) {
            action = values()[actionIndex];
        }
        return action;
    }
}
