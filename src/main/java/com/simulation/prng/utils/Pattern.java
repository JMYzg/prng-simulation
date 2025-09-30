package com.simulation.prng.utils;

public class Pattern {

    public static Long apply(long a, long b, int l, String f) {
        long product = a * b;

        String formatted = String.format(f, product);

        int start = l / 2;
        int end = start + l;

        String middle = formatted.substring(start, end);

        return Long.parseLong(middle);
    }
}
