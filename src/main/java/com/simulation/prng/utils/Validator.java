package com.simulation.prng.utils;

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
}
