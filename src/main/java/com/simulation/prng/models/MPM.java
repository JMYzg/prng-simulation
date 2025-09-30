package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.Pattern;

public class MPM extends AlgorithmTemplate {

    private final long seed1;
    private final long seed2;

    private long current1;
    private long current2;
    private int length;
    private String format;

    public MPM(long seed1, long seed2) {
        this.seed1 = seed1;
        this.seed2 = seed2;
    }

    @Override
    protected void initialize() {
        this.current1 = seed1;
        this.current2 = seed2;
        this.length = String.valueOf(this.current1).length();
        this.format = "%0"+ (2 * this.length) + "d";
    }

    @Override
    protected long next() {
        return Pattern.apply(current1, current2, length, format);
    }

    @Override
    protected long divisor() {
        return (long) Math.pow(10, this.length);
    }

    @Override
    protected void update(long next) {
        this.current1 = this.current2;
        this.current2 = next;
    }
}
