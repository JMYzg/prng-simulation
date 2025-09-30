package com.simulation.prng.models;

import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.Pattern;

public class MSM extends AlgorithmTemplate {

    private final long seed;

    private long current;
    private int length;
    private String format;

    public MSM(long seed) {
        this.seed = seed;
    }

    @Override
    protected void initialize() {
        this.current = this.seed;
        this.length = String.valueOf(this.current).length();
        this.format = "%0"+ (2 * this.length) + "d";
    }

    @Override
    protected long next() {
        return Pattern.apply(current, current, length, format);
    }

    @Override
    protected long divisor() {
        return (long) Math.pow(10, this.length);
    }

    @Override
    protected void update(long next) {
        this.current = next;
    }
}
