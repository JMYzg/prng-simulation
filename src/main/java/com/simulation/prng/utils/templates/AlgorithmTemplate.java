package com.simulation.prng.utils.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class AlgorithmTemplate {

    public final List<Double> generate() {
        List<Double> sequence = new ArrayList<>();
        HashSet<Long> uniques = new HashSet<>();

        initialize();

        long divisor = divisor();

        while (true) {
            long next = next();
            if (uniques.add(next)) {
                sequence.add((double) next / divisor);
                update(next);
            } else break;
        }
        return sequence;
    }

    protected abstract void initialize();
    protected abstract long next();
    protected abstract long divisor();
    protected abstract void update(long next);

}
