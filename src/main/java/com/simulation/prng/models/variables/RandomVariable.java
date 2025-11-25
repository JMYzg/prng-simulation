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
}
