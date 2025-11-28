package com.simulation.prng.models.variables.tests;

import com.simulation.prng.models.variables.RandomVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implements the Kolmogorov-Smirnov Goodness of Fit Test.
 * Only applicable for continuous distributions.
 */
public class KolmogorovSmirnovTest {

    public static class Result {
        public double dStatistic;
        public double criticalValue;
        public boolean passed;
        public double dPlus;
        public double dMinus;

        public Result(double dStatistic, double criticalValue, boolean passed, double dPlus, double dMinus) {
            this.dStatistic = dStatistic;
            this.criticalValue = criticalValue;
            this.passed = passed;
            this.dPlus = dPlus;
            this.dMinus = dMinus;
        }
    }

    public Result runTest(List<Double> data, RandomVariable distribution, double... params) {
        if (!distribution.isContinuous()) {
            throw new IllegalArgumentException("Kolmogorov-Smirnov test is only for continuous distributions.");
        }

        int N = data.size();
        if (N == 0)
            throw new IllegalArgumentException("Data cannot be empty");

        // Sort data
        List<Double> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        double dPlusMax = 0.0;
        double dMinusMax = 0.0;

        for (int i = 0; i < N; i++) {
            double x = sortedData.get(i);
            double theoreticalCDF = distribution.cdf(x, params);

            // Empirical CDF: i+1 / N
            double empiricalCDF = (double) (i + 1) / N;
            double empiricalCDFPrev = (double) i / N;

            // D+ = max(i/N - F(x)) -> Wait, standard definition:
            // D+ = max( (i+1)/N - F(xi) )
            // D- = max( F(xi) - i/N )

            double dp = empiricalCDF - theoreticalCDF;
            double dm = theoreticalCDF - empiricalCDFPrev;

            if (dp > dPlusMax)
                dPlusMax = dp;
            if (dm > dMinusMax)
                dMinusMax = dm;
        }

        double dStatistic = Math.max(dPlusMax, dMinusMax);
        double criticalValue = getCriticalValue(N, 0.05);
        boolean passed = dStatistic < criticalValue;

        return new Result(dStatistic, criticalValue, passed, dPlusMax, dMinusMax);
    }

    // Critical values for Alpha = 0.05
    private double getCriticalValue(int n, double alpha) {
        // Table for alpha = 0.05
        if (n <= 35) {
            double[] table = {
                    0.0, 0.975, 0.842, 0.708, 0.624, 0.565, 0.521, 0.486, 0.457, 0.432, 0.410,
                    0.391, 0.375, 0.361, 0.349, 0.338, 0.328, 0.318, 0.309, 0.301, 0.294,
                    0.287, 0.281, 0.275, 0.269, 0.264, 0.259, 0.254, 0.250, 0.246, 0.242,
                    0.238, 0.234, 0.231, 0.227, 0.224
            };
            if (n < table.length)
                return table[n];
        }

        // Approximation for N > 35: 1.36 / sqrt(N)
        return 1.36 / Math.sqrt(n);
    }
}
