package com.parkinglot.utils;

// Validates user input for the console application
public final class InputValidator {

    private InputValidator() {
        // utility class
    }

    // Checks if a license plate is non-null and non-blank
    public static boolean isValidLicensePlate(String plate) {
        return plate != null && !plate.isBlank() && plate.length() >= 2;
    }

    // Checks if a menu choice is within range
    public static boolean isValidMenuChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }

    // Checks if an amount is positive
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }

    // Checks if a string is non-null and non-blank
    public static boolean isNonBlank(String value) {
        return value != null && !value.isBlank();
    }

    // Parses an integer safely, returns -1 on failure
    public static int parseIntSafe(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
