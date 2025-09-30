package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;

public class LCG extends AlgorithmTemplate {

    private final long seed;
    private final long multiplier;
    private final long increment;
    private final long modulus;

    private long current;

    public LCG(long seed, long multiplier, long increment, long modulus) {
        this.seed = seed;
        this.multiplier = multiplier;
        this.increment = increment;
        this.modulus = modulus;
    }

    @Override
    protected void initialize() {
        this.current = this.seed;
    }

    @Override
    protected long next() {
        return (this.multiplier * this.current + this.increment) % modulus;
    }

    @Override
    protected long divisor() {
        return modulus - 1;
    }

    @Override
    protected void update(long next) {

    }
}
