package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class LCG {

    public static List<Long> generate(long seed, long multiplier, long increment, long modulus, int iterations) {

        List<Long> sequence = new ArrayList<>();
        long current = seed;

        for (int i = 0; i < iterations; i++) {
            current = (multiplier * current + increment) % modulus;
            sequence.add(current);
        }

        return sequence;
    }
}
