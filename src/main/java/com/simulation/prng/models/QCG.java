package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;

public class QCG extends AlgorithmTemplate {

    private final long seed;
    private final long a;
    private final long b;
    private final long c;
    private final long modulus;

    private long current;

    public QCG(long seed, long a, long b, long c, long modulus) {
        this.seed = seed;
        this.a = a;
        this.b = b;
        this.c = c;
        this.modulus = modulus;
    }

    @Override
    protected void initialize() {
        this.current = this.seed;
    }

    @Override
    protected long next() {
        long nextValue = (a * (current * current) + b * current + c) % modulus;
        if (nextValue < 0) {
            nextValue += modulus;
        }
        return nextValue;
    }

    @Override
    protected long divisor() {
        return 1;
    }

    @Override
    protected void update(long next) {
        this.current = next;
    }
}