package com.simulation.prng.models;

import com.simulation.prng.utils.AlertHandler;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import javafx.scene.control.Alert;

public class BBS extends AlgorithmTemplate {

    private final long seed;
    private final long p;
    private final long q;
    private long modulus;

    private long current;

    public BBS(long seed, long p, long q) {
        if (p % 4 != 3 || q % 4 != 3) {
            throw new IllegalArgumentException();
        }
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