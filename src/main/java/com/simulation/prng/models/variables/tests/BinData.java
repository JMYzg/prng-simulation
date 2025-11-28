package com.simulation.prng.models.variables.tests;

/**
 * Data class representing a bin (interval) for the Chi-Square test.
 * Used to store observed and expected frequencies for a specific range.
 */
public class BinData {
    private String intervalLabel;
    private double observed;
    private double expected;
    private double chiTerm; // (O - E)^2 / E

    public BinData(String intervalLabel, double observed, double expected) {
        this.intervalLabel = intervalLabel;
        this.observed = observed;
        this.expected = expected;
        this.chiTerm = (expected > 0) ? Math.pow(observed - expected, 2) / expected : 0.0;
    }

    public String getIntervalLabel() {
        return intervalLabel;
    }

    public void setIntervalLabel(String intervalLabel) {
        this.intervalLabel = intervalLabel;
    }

    public double getObserved() {
        return observed;
    }

    public void setObserved(double observed) {
        this.observed = observed;
        recalculateChiTerm();
    }

    public double getExpected() {
        return expected;
    }

    public void setExpected(double expected) {
        this.expected = expected;
        recalculateChiTerm();
    }

    public double getChiTerm() {
        return chiTerm;
    }

    private void recalculateChiTerm() {
        this.chiTerm = (expected > 0) ? Math.pow(observed - expected, 2) / expected : 0.0;
    }
}
