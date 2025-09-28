package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.List;

public class ACG {

    public static List<Long> generate(List<Long> seeds, long modulus, int iterations) {

        List<Long> sequence = new ArrayList<>(seeds);
        int k = seeds.size();

        for (int i = 0; i < iterations; i++) {
            int n = sequence.size();

            long term1 = sequence.get(n - 1);
            long term2 = sequence.get(n - k);
            long current = (term1 + term2) % modulus;

            sequence.add(current);
        }

        return sequence;
    }
}
