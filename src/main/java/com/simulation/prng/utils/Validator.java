package com.simulation.prng.utils;

public class Validator {

    public static boolean isPrime(long n) {
        return n % 2 != 0;
    }

    public static boolean isNatural(long n) {
        return n > 0;
    }
}
