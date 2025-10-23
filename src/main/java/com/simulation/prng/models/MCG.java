package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;

public class MCG extends AlgorithmTemplate {

    private final long seed;
    private final long multiplier;
    private final long modulus;

    private long current;

    public MCG(long seed, long multiplier, long modulus) {
        this.seed = seed;
        this.multiplier = multiplier;
        this.modulus = modulus;
    }

    @Override
    protected void initialize() {
        this.current = this.seed;
    }

    @Override
    protected long next() {
        return (this.multiplier * this.current) % modulus;
    }

    @Override
    protected long divisor() {
        return modulus - 1;
    }

    @Override
    protected void update(long next) {
        this.current = next;
    }
}
