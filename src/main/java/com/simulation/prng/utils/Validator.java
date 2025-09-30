package com.simulation.prng.utils;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static <T extends Number> boolean isPrime(T n) {
        return n.longValue() % 2 != 0;
    }

    @SafeVarargs
    public static <T extends Number> boolean isNotNatural(T... n) {
        for (T t : n) {
            if (t.longValue() > 0) return false;
        }
        return true;
    }

    @SafeVarargs
    public static <T extends String> boolean isEmpty (T... s) {
        for (T t : s) {
            if (!t.isEmpty()) return false;
        }
        return true;
    }

    public static List<Long> parseCSVFormat (String csv) {
        if (Validator.isEmpty(csv)) return new ArrayList<>();

        String[] parts = csv.trim().split("\\s*,\\s*");
        List<Long> numbers = new ArrayList<>();

        try {
            for (String part : parts) {
                long number = Long.parseLong(part);
                if (isNotNatural(number)) {
                    AlertHandler.showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Conversion cancelled",
                            "A number that not follows the format was found\n" +
                                    "Please check your csv format"
                    );
                    return null;
                } else numbers.add(number);
            }
            return numbers;

        } catch (NumberFormatException e) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Incorrect format",
                    "Please follow the format\n" +
                            "Code error: " + e.getMessage()
            );
            return null;
        }
    }

    public static boolean isPowerOfTwo (long m) {
        return (m > 0) && ((m & (m - 1)) == 0);
    }
}
