package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class BBS {

    public static List<Long> generate(long seed, long p, long q, int iterations) {

        List<Long> sequence = new ArrayList<>();
        long modulus = p * q;

        for (int i = 0; i < iterations; i++) {
            long current = (seed * seed) % modulus;

            sequence.add(current);
            seed = current;
        }
        return sequence;
    }
}
