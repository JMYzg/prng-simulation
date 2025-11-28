package com.simulation.prng.models.variables.tests;

import com.simulation.prng.models.variables.RandomVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Implements the Chi-Square Goodness of Fit Test.
 */
public class ChiSquareTest {

    public static class Result {
        public double statistic;
        public int degreesOfFreedom;
        public double criticalValue;
        public boolean passed;
        public List<BinData> binData;

        public Result(double statistic, int degreesOfFreedom, double criticalValue, boolean passed,
                List<BinData> binData) {
            this.statistic = statistic;
            this.degreesOfFreedom = degreesOfFreedom;
            this.criticalValue = criticalValue;
            this.passed = passed;
            this.binData = binData;
        }
    }

    public Result runTest(List<Double> data, RandomVariable distribution, double... params) {
        int N = data.size();
        if (N < 5) {
            throw new IllegalArgumentException("Data size too small for Chi-Square test (min 5)");
        }

        int k = (int) Math.sqrt(N);
        if (k < 3)
            k = 3; // Minimum bins

        List<BinData> bins = new ArrayList<>();
        double min = Collections.min(data);
        double max = Collections.max(data);

        // Adjust min/max slightly to include boundary values
        // For discrete, we might need a different approach, but let's try a unified one
        // or separate.

        if (distribution.isContinuous()) {
            double range = max - min;
            double step = range / k;

            for (int i = 0; i < k; i++) {
                double lower = min + i * step;
                double upper = (i == k - 1) ? max : min + (i + 1) * step; // Ensure last bin covers max

                // Observed
                int observed = 0;
                for (double val : data) {
                    if (val >= lower && (i == k - 1 ? val <= upper : val < upper)) {
                        observed++;
                    }
                }

                // Expected
                // E = N * (CDF(upper) - CDF(lower))
                double prob = distribution.cdf(upper, params) - distribution.cdf(lower, params);
                double expected = N * prob;

                bins.add(new BinData(String.format("[%.4f, %.4f)", lower, upper), observed, expected));
            }
        } else {
            // Discrete (Poisson, Bernoulli, Binomial)
            // For discrete, bins are usually specific values or ranges of integers.
            // Let's map unique values to bins first, then group if needed.
            // Or just use the range min to max integers.

            int minInt = (int) Math.floor(min);
            int maxInt = (int) Math.ceil(max);

            // If range is huge, we might need to group, but for typical simulation tasks
            // (Poisson/Binomial with small params),
            // individual values might work. However, if k is small, we should group.
            // Let's stick to the Professor's "Binning" instruction: k = sqrt(N).
            // But for discrete, "bins" are natural values.
            // If maxInt - minInt + 1 > k, we group.

            // Simple approach: Treat as continuous intervals for binning, but calculate
            // Expected using sum of probabilities?
            // Or better: Iterate through all possible integer values from min to max.

            // Let's try to group by integer values first.
            for (int x = minInt; x <= maxInt; x++) {
                int observed = 0;
                for (double val : data) {
                    if (Math.abs(val - x) < 1e-9)
                        observed++;
                }

                double expected = N * distribution.getProbability(x, params);
                bins.add(new BinData(String.valueOf(x), observed, expected));
            }
        }

        // Step 3: Rule of 5 (Merge bins)
        bins = mergeBins(bins);

        // Step 4: Calculate Statistic
        double chiSquare = 0.0;
        for (BinData bin : bins) {
            chiSquare += bin.getChiTerm();
        }

        // Step 5: Result
        // m = number of estimated parameters. Usually 0 if we test against known
        // params.
        // If we estimated params from data, m would be > 0. Assuming known params for
        // now (m=0).
        // DoF = k - m - 1
        int m = 0;
        int dof = bins.size() - m - 1;
        if (dof < 1)
            dof = 1; // Fallback

        double criticalValue = getCriticalValue(dof, 0.05);
        boolean passed = chiSquare < criticalValue;

        return new Result(chiSquare, dof, criticalValue, passed, bins);
    }

    private List<BinData> mergeBins(List<BinData> originalBins) {
        List<BinData> merged = new ArrayList<>();
        if (originalBins.isEmpty())
            return merged;

        BinData current = originalBins.get(0);

        for (int i = 1; i < originalBins.size(); i++) {
            BinData next = originalBins.get(i);

            if (current.getExpected() < 5) {
                // Merge with next
                // Update label
                String newLabel = current.getIntervalLabel().split(",")[0] + " - " + next.getIntervalLabel();
                if (current.getIntervalLabel().contains("[")) {
                    // Try to keep format [a, b)
                    String start = current.getIntervalLabel().substring(0, current.getIntervalLabel().indexOf(","));
                    String end = next.getIntervalLabel();
                    if (end.contains(","))
                        end = end.substring(end.indexOf(",") + 1);
                    newLabel = start + "," + end;
                }

                current.setIntervalLabel(newLabel);
                current.setObserved(current.getObserved() + next.getObserved());
                current.setExpected(current.getExpected() + next.getExpected());
            } else {
                merged.add(current);
                current = next;
            }
        }

        // Check last one
        if (current.getExpected() < 5 && !merged.isEmpty()) {
            // Merge with previous (last in merged)
            BinData prev = merged.remove(merged.size() - 1);

            String newLabel = prev.getIntervalLabel() + " + " + current.getIntervalLabel();
            if (prev.getIntervalLabel().contains("[")) {
                String start = prev.getIntervalLabel().substring(0, prev.getIntervalLabel().indexOf(","));
                String end = current.getIntervalLabel();
                if (end.contains(","))
                    end = end.substring(end.indexOf(",") + 1);
                newLabel = start + "," + end;
            }

            prev.setIntervalLabel(newLabel);
            prev.setObserved(prev.getObserved() + current.getObserved());
            prev.setExpected(prev.getExpected() + current.getExpected());
            merged.add(prev);
        } else {
            merged.add(current);
        }

        return merged;
    }

    // Critical values for Alpha = 0.05
    private double getCriticalValue(int dof, double alpha) {
        // Simple lookup for common DoF
        double[] table = {
                0.0, 3.841, 5.991, 7.815, 9.488, 11.070, 12.592, 14.067, 15.507, 16.919, 18.307,
                19.675, 21.026, 22.362, 23.685, 24.996, 26.296, 27.587, 28.869, 30.144, 31.410,
                32.671, 33.924, 35.172, 36.415, 37.652, 38.885, 40.113, 41.337, 42.557, 43.773
        };

        if (dof < table.length)
            return table[dof];

        // Approximation for large DoF: 0.5 * (z + sqrt(2*dof - 1))^2 where z = 1.645
        // for 0.05
        // Or Wilson-Hilferty approximation: dof * (1 - 2/(9*dof) + z *
        // sqrt(2/(9*dof)))^3
        double z = 1.645;
        return dof * Math.pow(1.0 - 2.0 / (9.0 * dof) + z * Math.sqrt(2.0 / (9.0 * dof)), 3);
    }
}
