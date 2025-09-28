package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class CMM {

    public static List<Long> generate(long constant, long seed, int iterations) {

        List<Long> sequence = new ArrayList<>();

        long current = seed;
        int length = String.valueOf(current).length();

        String format = "%0"+ (2 + length) + "d";

        for (int i = 0; i < iterations; i++) {
            long product = constant * current;

            String formatted = String.format(format, product);

            int start = length / 2;
            int end = start + length;

            String middle = formatted.substring(start, end);

            long random = Long.parseLong(middle);

            sequence.add(random);

            current = random;
        }
        return sequence;
    }
}
