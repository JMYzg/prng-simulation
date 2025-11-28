package com.simulation.prng.models.variables;

/**
 * Implementation of a Normal (Gaussian) Distribution using the Convolution
 * Method.
 * 
 * The Normal Distribution is one of the most important probability
 * distributions,
 * characterized by its bell-shaped curve.
 * 
 * Algorithm: Central Limit Theorem approximation using 12 uniform random
 * numbers.
 * Formula: x = ((sum of 12 random numbers) - 6) * σ + μ
 * 
 * This approximation is based on the fact that the sum of 12 U(0,1) random
 * variables
 * has mean 6 and variance 1, which can be scaled to any normal distribution.
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class NormalDist implements RandomVariable {

    /**
     * Generates a normally distributed random value.
     * 
     * Uses the Convolution Method (Central Limit Theorem):
     * - Sum 12 uniform(0,1) random numbers
     * - Subtract 6 (to get mean 0, variance 1)
     * - Scale by standard deviation and add mean: ((sum - 6) * σ) + μ
     * 
     * @param ri     the first pseudo-random number from a uniform distribution (0,
     *               1)
     * @param params distribution parameters:
     *               params[0] = mean (μ, can be any real number)
     *               params[1] = stdDev (σ, must be > 0)
     *               params[2..13] = 11 additional random numbers (total 12
     *               including ri)
     * @return a random value following a normal distribution N(μ, σ²)
     * @throws IllegalArgumentException if parameters are invalid or insufficient
     *                                  random numbers
     */
    @Override
    public double generate(java.util.Iterator<Double> prnIterator, double... params) {
        // Validate parameters (mean, stdDev)
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "Normal distribution requires exactly 2 parameters: mean and stdDev");
        }

        double mean = params[0];
        double stdDev = params[1];

        // Validate stdDev
        if (stdDev <= 0) {
            throw new IllegalArgumentException(
                    "Invalid parameter: standard deviation must be positive (σ > 0). Got stdDev=" + stdDev);
        }

        // Apply Convolution Method: sum 12 random numbers
        double sum = 0.0;

        // Consume 12 random numbers
        for (int i = 0; i < 12; i++) {
            if (!prnIterator.hasNext()) {
                throw new java.util.NoSuchElementException(
                        "Insufficient random numbers for Normal distribution (needs 12)");
            }
            double ri = prnIterator.next();

            // Validate each random number
            if (ri < 0.0 || ri > 1.0) {
                throw new IllegalArgumentException(
                        "Random number at index " + i + " must be in the range [0, 1]. Got " + ri);
            }
            sum += ri;
        }

        // Apply the Central Limit Theorem formula:
        // Z = sum - 6 (standard normal: mean=0, variance=1)
        // X = Z * σ + μ (scale and shift to desired distribution)
        return ((sum - 6.0) * stdDev) + mean;
    }

    @Override
    public boolean isContinuous() {
        return true;
    }

    /**
     * Calculates the PDF of the Normal distribution.
     * f(x) = (1 / (sigma * sqrt(2*pi))) * e^(-0.5 * ((x - mu) / sigma)^2)
     */
    @Override
    public double getProbability(double x, double... params) {
        if (params.length < 2)
            throw new IllegalArgumentException("Normal requires mean and stdDev");
        double mean = params[0];
        double stdDev = params[1];
        if (stdDev <= 0)
            throw new IllegalArgumentException("stdDev must be > 0");

        return (1.0 / (stdDev * Math.sqrt(2 * Math.PI))) *
                Math.exp(-0.5 * Math.pow((x - mean) / stdDev, 2));
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Normal"
     */
    @Override
    public String getDistributionName() {
        return "Normal";
    }

    /**
     * Calculates the Cumulative Distribution Function (CDF) at x.
     * 
     * @param x      the value to evaluate
     * @param params params[0] = mean, params[1] = stdDev
     * @return P(X <= x)
     */
    @Override
    public double cdf(double x, double... params) {
        if (params.length < 2) {
            throw new IllegalArgumentException("Normal distribution requires mean and stdDev for CDF");
        }
        double mean = params[0];
        double stdDev = params[1];

        org.apache.commons.math3.distribution.NormalDistribution normal = new org.apache.commons.math3.distribution.NormalDistribution(
                mean, stdDev);
        return normal.cumulativeProbability(x);
    }
}
