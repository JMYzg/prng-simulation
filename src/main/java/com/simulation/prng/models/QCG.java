package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class QCG {

    public static List<Double> generate(long seed, long a, long b, long c, long modulus) {
        List<Double> sequence = new ArrayList<>();
        HashSet<Long> uniques = new HashSet<>();

        long current = seed;

        while (true) {
            long squared = current * current;

            long A = a * squared;
            long B = b * current;

            current = (A + B + c) % modulus;

            if (current < 0) current += modulus;

            if (uniques.add(current)) sequence.add((double) current);
            else break;
        }

        return sequence;
    }
}
