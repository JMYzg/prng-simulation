package com.simulation.prng.utils;

public class Validator {

    public static <T extends Number> boolean isPrime(T n) {
        return n.longValue() % 2 != 0;
    }

    public static <T extends Number> boolean isNatural (T n) {
        return n.longValue() > 0;
    }
}
