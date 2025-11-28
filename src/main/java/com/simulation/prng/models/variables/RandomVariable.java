package com.simulation.prng.models.variables;

/**
 * Interface for generating random variables from specific statistical
 * distributions.
 * 
 * This interface defines the contract for transforming pseudo-random numbers
 * (0-1)
 * into specific statistical distributions using methods like the Inverse
 * Transform Method.
 * 
 * Implementations should be stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public interface RandomVariable {

    /**
     * Generates a random value from this distribution.
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      additional distribution-specific parameters (e.g., mean,
     *                    variance, lambda)
     * @return a random value following this distribution
     * @throws IllegalArgumentException         if parameters are invalid for this
     *                                          distribution
     * @throws java.util.NoSuchElementException if not enough random numbers are
     *                                          available
     */
    double generate(java.util.Iterator<Double> prnIterator, double... params);

    /**
     * Returns the name of this distribution.
     * 
     * @return the distribution name (e.g., "Uniform", "Exponential")
     */
    String getDistributionName();

    /**
     * Returns true if the random variable is continuous, false otherwise.
     * 
     * @return true if continuous, false if discrete
     */
    boolean isContinuous();

    /**
     * Calculates the probability mass function (PMF) for discrete variables
     * or probability density function (PDF) for continuous variables at x.
     * 
     * @param x      the value to evaluate
     * @param params distribution parameters
     * @return P(X = x) for discrete, f(x) for continuous
     */
    double getProbability(double x, double... params);

    /**
     * Calculates the Cumulative Distribution Function (CDF) at x.
     * Used for Kolmogorov-Smirnov and Anderson-Darling tests.
     * 
     * @param x      the value to evaluate
     * @param params distribution parameters
     * @return P(X <= x)
     */
    default double cdf(double x, double... params) {
        throw new UnsupportedOperationException("CDF not implemented for this distribution");
    }
}
