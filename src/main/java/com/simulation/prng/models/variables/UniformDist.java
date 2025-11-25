package com.simulation.prng.models.variables;

/**
 * Implementation of a Uniform Distribution using the Inverse Transform Method.
 * 
 * The Uniform Distribution generates random values uniformly distributed
 * between
 * a minimum value 'a' and a maximum value 'b'.
 * 
 * Formula: x = a + (b - a) * ri
 * where:
 * - a: minimum value (lower bound)
 * - b: maximum value (upper bound)
 * - ri: pseudo-random number from uniform distribution (0, 1)
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class UniformDist implements RandomVariable {

    /**
     * Generates a uniformly distributed random value.
     * 
     * Uses the Inverse Transform Method: x = a + (b - a) * ri
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = a (minimum value)
     *                    params[1] = b (maximum value)
     * @return a random value following a uniform distribution between a and b
     * @throws IllegalArgumentException         if params length is not 2, if a >=
     *                                          b,
     *                                          or if ri is not in the range [0, 1]
     * @throws java.util.NoSuchElementException if not enough random numbers are
     *                                          available
     */
    @Override
    public double generate(java.util.Iterator<Double> prnIterator, double... params) {
        // Validate input parameters
        if (params.length != 2) {
            throw new IllegalArgumentException(
                    "Uniform distribution requires exactly 2 parameters: min (a) and max (b)");
        }

        double a = params[0];
        double b = params[1];

        if (a >= b) {
            throw new IllegalArgumentException(
                    "Invalid parameters: min (a) must be less than max (b). Got a=" + a + ", b=" + b);
        }

        // Consume one random number
        if (!prnIterator.hasNext()) {
            throw new java.util.NoSuchElementException("Insufficient random numbers for Uniform distribution");
        }
        double ri = prnIterator.next();

        // Validate random number
        if (ri < 0.0 || ri > 1.0) {
            throw new IllegalArgumentException(
                    "Random number 'ri' must be in the range [0, 1]. Got ri=" + ri);
        }

        // Apply Inverse Transform Method: x = a + (b - a) * ri
        return a + (b - a) * ri;
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Uniform"
     */
    @Override
    public String getDistributionName() {
        return "Uniform";
    }
}
