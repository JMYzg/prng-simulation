package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class MPM {

    public static List<Long> generate(long seed1, long seed2, int iterations) {
        List<Long> sequence = new ArrayList<>();

        long current1 = seed1;
        long current2 = seed2;

        // both seeds need to match
        int length = String.valueOf(current1).length();

        String format = "%0"+ (2 + length) + "d";

        for (int i = 0; i < iterations; i++) {
            long product = current1 * current2;

            String formatted = String.format(format, product);

            int start = length / 2;
            int end = start + length;

            String middle = formatted.substring(start, end);

            long next = Long.parseLong(middle);

            sequence.add(next);

            current1 = current2;
            current2 = next;
        }
        return sequence;
    }
}
