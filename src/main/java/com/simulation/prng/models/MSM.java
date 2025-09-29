package com.simulation.prng.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MSM {

    public static List<Double> generate(long seed, int iterations) {

        List<Double> sequence = new ArrayList<>();
        HashSet<Double> set = new HashSet<>();

        long current = seed;
        int length = String.valueOf(current).length();
        long divisor = (long) Math.pow(10, length);
        boolean iterate = true;

        String format = "%0"+ (2 + length) + "d";

        while (iterate) {
            long squared = current * current;

            String formatted = String.format(format, squared);

            int start = length / 2;
            int end = start + length;

            String middle = formatted.substring(start, end);

            current = Long.parseLong(middle);

            sequence.add((double) current / divisor);

        }
        return sequence;
    }
}
