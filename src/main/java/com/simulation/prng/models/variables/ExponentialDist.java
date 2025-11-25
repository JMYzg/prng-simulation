package com.simulation.prng.models.variables;

/**
 * Implementation of an Exponential Distribution using the Inverse Transform
 * Method.
 * 
 * The Exponential Distribution is commonly used to model time between events in
 * a
 * Poisson process, such as the time between arrivals in a queue.
 * 
 * Formula: x = -ln(1 - ri) / lambda
 * where:
 * - lambda: rate parameter (λ > 0), representing the average number of events
 * per unit time
 * - ri: pseudo-random number from uniform distribution (0, 1)
 * 
 * The mean of the distribution is 1/lambda, and the variance is 1/lambda².
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class ExponentialDist implements RandomVariable {

    /**
     * Generates an exponentially distributed random value.
     * 
     * Uses the Inverse Transform Method: x = -ln(1 - ri) / lambda
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = lambda (rate parameter, must be > 0)
     * @return a random value following an exponential distribution with rate lambda
     * @throws IllegalArgumentException         if params length is not 1, if lambda
     *                                          <= 0,
     *                                          or if ri is not in the range (0, 1)
     * @throws java.util.NoSuchElementException if not enough random numbers are
     *                                          available
     */
    @Override
    public double generate(java.util.Iterator<Double> prnIterator, double... params) {
        // Validate input parameters
        if (params.length != 1) {
            throw new IllegalArgumentException(
                    "Exponential distribution requires exactly 1 parameter: lambda (rate)");
        }

        double lambda = params[0];

        if (lambda <= 0) {
            throw new IllegalArgumentException(
                    "Invalid parameter: lambda must be positive. Got lambda=" + lambda);
        }

        // Consume one random number
        if (!prnIterator.hasNext()) {
            throw new java.util.NoSuchElementException("Insufficient random numbers for Exponential distribution");
        }
        double ri = prnIterator.next();

        // Validate random number
        if (ri < 0.0 || ri > 1.0) {
            throw new IllegalArgumentException(
                    "Random number 'ri' must be in the range [0, 1]. Got ri=" + ri);
        }

        // Apply Inverse Transform Method: x = -ln(1 - ri) / lambda
        return -Math.log(1.0 - ri) / lambda;
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Exponential"
     */
    @Override
    public String getDistributionName() {
        return "Exponential";
    }
}
