package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;

public class BBS extends AlgorithmTemplate {

    private final long seed;
    private final long p;
    private final long q;
    private long modulus;

    private long current;

    public BBS(long seed, long p, long q) {
        this.seed = seed;
        this.p = p;
        this.q = q;
    }

    @Override
    protected void initialize() {
        this.current = this.seed;
        this.modulus = p * q;
    }

    @Override
    protected long next() {
        return (this.current * this.current) % this.modulus;
    }

    @Override
    protected long divisor() {
        return this.modulus - 1;
    }

    @Override
    protected void update(long next) {
        this.current = next;
    }
}
