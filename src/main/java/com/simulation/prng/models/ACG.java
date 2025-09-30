package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ACG {

    public static List<Double> generate(List<Long> seeds, long modulus) {
        List<Long> sequence = new ArrayList<>(seeds);
        HashSet<Long> uniques = new HashSet<>();

        int k = seeds.size();

        while (true) {
            int n = sequence.size();

            long term1 = sequence.get(n - 1);
            long term2 = sequence.get(n - k);
            long current = (term1 + term2) % modulus;

            if (uniques.add(current)) sequence.add(current / modulus - 1);
             else break;
        }

        return sequence.stream().map(Long::doubleValue).collect(Collectors.toList());
    }
}
