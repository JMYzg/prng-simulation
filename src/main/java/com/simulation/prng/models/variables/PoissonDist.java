package com.simulation.prng.models.variables;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of a Poisson Distribution using the Inverse Transform Method.
 * 
 * The Poisson Distribution models the number of events occurring in a fixed
 * interval of time or space, given a constant average rate λ (lambda).
 * 
 * Algorithm: Iterative multiplication of random numbers until the product is
 * less than e^(-λ).
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class PoissonDist implements RandomVariable {

    /**
     * Generates a Poisson distributed random value.
     * 
     * Uses the Inverse Transform Method with iterative multiplication:
     * - Initialize: product = 1, count = 0
     * - While product >= e^(-lambda):
     * - product *= next random number
     * - count++
     * - Return count - 1
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = lambda (rate parameter, must be > 0)
     * @return a random value following a Poisson distribution with rate lambda
     * @throws IllegalArgumentException if parameters are invalid
     * @throws NoSuchElementException   if not enough random numbers are available
     */
    @Override
    public double generate(Iterator<Double> prnIterator, double... params) {
        // Validate parameters
        if (params == null || params.length < 1) {
            throw new IllegalArgumentException("Poisson distribution requires lambda parameter");
        }

        double lambda = params[0];

        if (lambda <= 0) {
            throw new IllegalArgumentException("Lambda must be greater than 0. Got: " + lambda);
        }

        // Apply Inverse Transform Method (iterative)
        double threshold = Math.exp(-lambda);
        double product = 1.0;
        int count = 0;

        // Continue consuming random numbers until product < threshold
        do {
            if (!prnIterator.hasNext()) {
                throw new NoSuchElementException("Insufficient random numbers for Poisson distribution");
            }
            double ri = prnIterator.next();

            // Validate random number
            if (ri <= 0.0 || ri >= 1.0) {
                // Ideally we should throw, but standard PRNs might include 0 or 1.
                // For this algorithm, 0 would cause immediate stop (product=0 < threshold),
                // which is valid.
                // 1 would not change product.
                // We'll proceed.
            }

            product *= ri;
            count++;
        } while (product >= threshold);

        return count - 1;
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Poisson"
     */
    @Override
    public String getDistributionName() {
        return "Poisson";
    }
}
