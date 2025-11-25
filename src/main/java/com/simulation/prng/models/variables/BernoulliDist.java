package com.simulation.prng.models.variables;

/**
 * Implementation of a Bernoulli Distribution using the Inverse Transform
 * Method.
 * 
 * The Bernoulli Distribution represents a single trial with two possible
 * outcomes:
 * success (1) with probability p, or failure (0) with probability 1-p.
 * 
 * Algorithm: Return 1 if ri <= p, else return 0.
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class BernoulliDist implements RandomVariable {

    /**
     * Generates a Bernoulli distributed random value (0 or 1).
     * 
     * Uses the Inverse Transform Method:
     * - Returns 1 (success) if ri <= p
     * - Returns 0 (failure) if ri > p
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = p (probability of success, must be in [0, 1])
     * @return 1.0 for success, 0.0 for failure
     * @throws IllegalArgumentException if p is not in [0, 1]
     */
    @Override
    public double generate(java.util.Iterator<Double> prnIterator, double... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException(
                    "Bernoulli distribution requires exactly 1 parameter: p (probability of success)");
        }

        double p = params[0];

        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("Probability p must be between 0 and 1");
        }

        if (!prnIterator.hasNext()) {
            throw new java.util.NoSuchElementException("Not enough random numbers available");
        }

        double ri = prnIterator.next();

        return (ri <= p) ? 1.0 : 0.0;
    }

    @Override
    public String getDistributionName() {
        return "Bernoulli";
    }
}
