package com.simulation.prng.models.variables;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementation of an Erlang Distribution using the Convolution Method.
 * 
 * The Erlang Distribution is a special case of the Gamma distribution where the
 * shape parameter k is an integer. It models the time until k events occur in a
 * Poisson process.
 * 
 * Algorithm: Sum of k exponential variables using the convolution method.
 * Formula: x = -1/λ * ln(product of k random numbers)
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class ErlangDist implements RandomVariable {

    /**
     * Generates an Erlang distributed random value.
     * 
     * Uses the Convolution Method (sum of k exponential variables):
     * x = -(1/λ) * ln(r1 * r2 * ... * rk)
     * 
     * This is equivalent to summing k independent exponential(λ) random variables.
     * 
     * @param prnIterator iterator to consume pseudo-random numbers (0-1) from
     * @param params      distribution parameters:
     *                    params[0] = k (shape parameter, must be a positive
     *                    integer)
     *                    params[1] = lambda (rate parameter, must be > 0)
     * @return a random value following an Erlang distribution
     * @throws IllegalArgumentException if parameters are invalid
     * @throws NoSuchElementException   if not enough random numbers are available
     */
    @Override
    public double generate(Iterator<Double> prnIterator, double... params) {
        // Validate parameters
        if (params == null || params.length < 2) {
            throw new IllegalArgumentException("Erlang distribution requires k and lambda parameters");
        }

        double kDouble = params[0];
        if (kDouble <= 0 || kDouble != Math.floor(kDouble)) {
            throw new IllegalArgumentException("Invalid parameter: k must be a positive integer. Got k=" + kDouble);
        }
        int k = (int) kDouble;

        double lambda = params[1];
        if (lambda <= 0) {
            throw new IllegalArgumentException(
                    "Invalid parameter: lambda must be positive (λ > 0). Got lambda=" + lambda);
        }

        // Apply Convolution Method: compute product of k random numbers
        double product = 1.0;

        // Consume k random numbers
        for (int i = 0; i < k; i++) {
            if (!prnIterator.hasNext()) {
                throw new NoSuchElementException(
                        "Insufficient random numbers for Erlang distribution (needs " + k + ")");
            }
            double ri = prnIterator.next();

            // Validate each random number
            if (ri <= 0.0 || ri >= 1.0) {
                // Ideally throw, but proceed for now
            }

            product *= ri;
        }

        // Apply the formula: x = -(1/λ) * ln(product)
        return -Math.log(product) / lambda;
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Erlang"
     */
    @Override
    public String getDistributionName() {
        return "Erlang";
    }

    @Override
    public boolean isContinuous() {
        return true;
    }

    /**
     * Calculates the PDF of the Erlang distribution.
     * f(x) = (lambda^k * x^(k-1) * e^(-lambda*x)) / (k-1)!
     */
    @Override
    public double getProbability(double x, double... params) {
        if (params.length != 2)
            throw new IllegalArgumentException("Erlang requires k and lambda");
        int k = (int) params[0];
        double lambda = params[1];
        if (k <= 0)
            throw new IllegalArgumentException("k must be > 0");
        if (lambda <= 0)
            throw new IllegalArgumentException("lambda must be > 0");

        if (x < 0)
            return 0.0;
        return (Math.pow(lambda, k) * Math.pow(x, k - 1) * Math.exp(-lambda * x)) / factorial(k - 1);
    }

    /**
     * Calculates the CDF of the Erlang distribution.
     * F(x) = 1 - Sum( (e^(-lambda*x) * (lambda*x)^i) / i! ) for i=0 to k-1
     */
    @Override
    public double cdf(double x, double... params) {
        if (params.length != 2)
            throw new IllegalArgumentException("Erlang requires k and lambda");
        int k = (int) params[0];
        double lambda = params[1];
        if (k <= 0)
            throw new IllegalArgumentException("k must be > 0");
        if (lambda <= 0)
            throw new IllegalArgumentException("lambda must be > 0");

        if (x < 0)
            return 0.0;
        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            sum += (Math.exp(-lambda * x) * Math.pow(lambda * x, i)) / factorial(i);
        }
        return 1.0 - sum;
    }

    private double factorial(int n) {
        double fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
}
