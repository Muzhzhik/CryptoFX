package com.example.cryptofx.utils;

public class Logger {
    public void error(String text) {
        info("Error: " + text, ConsoleColors.RED_BOLD_BRIGHT);
    }

    public void info(String text) {
        System.out.print(text);
    }

    public void info(String text, String color) {
        info(color + text);
        System.out.print(ConsoleColors.RESET);
    }
}
