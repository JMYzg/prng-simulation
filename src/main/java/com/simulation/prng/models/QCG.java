package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class QCG {

    public static List<Long> generate(long seed, long a, long b, long c, long modulus, int iterations) {

        List<Long> sequence = new ArrayList<>();

        long current = seed;

        for (int i = 0; i < iterations; i++) {
            long squared = current * current;

            long A = a * squared;
            long B = b * current;

            current = (A + B + c) % modulus;

            if (current < 0) current += modulus;

            sequence.add(current);
        }

        return sequence;
    }
}
