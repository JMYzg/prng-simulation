package com.simulation.prng.models.variables;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of a Binomial Distribution using the Composition Method.
 * 
 * The Binomial Distribution models the number of successes in n independent
 * Bernoulli trials, each with probability p of success.
 * 
 * Algorithm: Execute Bernoulli logic n times and sum the successes.
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class BinomialDist implements RandomVariable {

    /**
     * Generates a binomially distributed random value.
     * 
     * Uses the Composition Method:
     * - Perform n Bernoulli trials
     * - For each trial i: if ri <= p, count as success
     * - Return total number of successes
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = n (number of trials, must be a positive
     *                    integer)
     *                    params[1] = p (probability of success, must be in [0, 1])
     * @return a random value following a Binomial distribution
     * @throws IllegalArgumentException if parameters are invalid
     * @throws NoSuchElementException   if not enough random numbers are available
     */
    @Override
    public double generate(Iterator<Double> prnIterator, double... params) {
        // Validate parameters
        if (params == null || params.length < 2) {
            throw new IllegalArgumentException("Binomial distribution requires n and p parameters");
        }

        double nDouble = params[0];
        if (nDouble <= 0 || nDouble != Math.floor(nDouble)) {
            throw new IllegalArgumentException("Invalid parameter: n must be a positive integer. Got n=" + nDouble);
        }
        int n = (int) nDouble;

        double p = params[1];
        if (p < 0.0 || p > 1.0) {
            throw new IllegalArgumentException(
                    "Invalid parameter: probability 'p' must be in the range [0, 1]. Got p=" + p);
        }

        // Apply Composition Method: sum n Bernoulli trials
        int successes = 0;

        for (int i = 0; i < n; i++) {
            if (!prnIterator.hasNext()) {
                throw new NoSuchElementException(
                        "Insufficient random numbers for Binomial distribution (needs " + n + ")");
            }
            double ri = prnIterator.next();

            // Validate random number
            if (ri < 0.0 || ri > 1.0) {
                // Ideally throw, but proceed for now as per standard PRN behavior
            }

            // Bernoulli trial: success if ri <= p
            if (ri <= p) {
                successes++;
            }
        }

        return successes;
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Binomial"
     */
    @Override
    public String getDistributionName() {
        return "Binomial";
    }
}
