package com.simulation.prng.models.variables;

/**
 * Implementation of a Triangular Distribution using the Inverse Transform
 * Method.
 * 
 * The Triangular Distribution is defined by three parameters: minimum (a),
 * maximum (b),
 * and mode (c). It has a triangular-shaped probability density function.
 * 
 * Algorithm: Piecewise inverse CDF based on where the random number falls
 * relative to the probability at the mode.
 * 
 * This class is stateless and thread-safe.
 * 
 * @author PRNG Simulation Team
 * @version 1.0
 */
public class TriangularDist implements RandomVariable {

    /**
     * Generates a triangularly distributed random value.
     * 
     * Uses the Inverse Transform Method with piecewise formula:
     * - If ri < (c-a)/(b-a): x = a + sqrt(ri * (b-a) * (c-a))
     * - Otherwise: x = b - sqrt((1-ri) * (b-a) * (b-c))
     * 
     * @param ri     the pseudo-random number from a uniform distribution (0, 1)
     * @param params distribution parameters:
     *               params[0] = a (minimum value)
     *               params[1] = b (maximum value)
     *               params[2] = c (mode, most likely value)
     * @return a random value following a triangular distribution
     * @throws IllegalArgumentException if parameters are invalid or constraints
     *                                  violated
     */
    @Override
    public double generate(java.util.Iterator<Double> prnIterator, double... params) {
        // Validate input parameters
        if (params.length != 3) {
            throw new IllegalArgumentException(
                    "Triangular distribution requires exactly 3 parameters: a (min), b (max), c (mode)");
        }

        double a = params[0];
        double b = params[1];
        double c = params[2];

        // Validate parameter constraints: a <= c <= b
        if (a >= b) {
            throw new IllegalArgumentException(
                    "Invalid parameters: minimum 'a' must be less than maximum 'b'. " +
                            "Got a=" + a + ", b=" + b);
        }

        if (c < a || c > b) {
            throw new IllegalArgumentException(
                    "Invalid parameters: mode 'c' must be between a and b (a <= c <= b). " +
                            "Got a=" + a + ", c=" + c + ", b=" + b);
        }

        // Consume one random number
        if (!prnIterator.hasNext()) {
            throw new java.util.NoSuchElementException("Insufficient random numbers for Triangular distribution");
        }
        double ri = prnIterator.next();

        // Validate random number
        if (ri < 0.0 || ri > 1.0) {
            throw new IllegalArgumentException(
                    "Random number 'ri' must be in the range [0, 1]. Got ri=" + ri);
        }

        // Apply Inverse Transform Method (piecewise)
        // Calculate the probability at the mode
        double fc = (c - a) / (b - a);

        // Piecewise inverse CDF
        if (ri < fc) {
            // Left side of the triangle: x = a + sqrt(ri * (b-a) * (c-a))
            return a + Math.sqrt(ri * (b - a) * (c - a));
        } else {
            // Right side of the triangle: x = b - sqrt((1-ri) * (b-a) * (b-c))
            return b - Math.sqrt((1.0 - ri) * (b - a) * (b - c));
        }
    }

    /**
     * Returns the name of this distribution.
     * 
     * @return "Triangular"
     */
    @Override
    public String getDistributionName() {
        return "Triangular";
    }

    @Override
    public boolean isContinuous() {
        return true;
    }

    /**
     * Calculates the PDF of the Triangular distribution.
     */
    @Override
    public double getProbability(double x, double... params) {
        if (params.length != 3)
            throw new IllegalArgumentException("Triangular requires a, b, c");
        double a = params[0];
        double b = params[1];
        double c = params[2];
        if (a >= b || c < a || c > b)
            throw new IllegalArgumentException("Invalid parameters: a <= c <= b");

        if (x < a || x > b)
            return 0.0;
        if (x < c)
            return 2 * (x - a) / ((b - a) * (c - a));
        if (Math.abs(x - c) < 1e-9)
            return 2 / (b - a);
        return 2 * (b - x) / ((b - a) * (b - c));
    }

    /**
     * Calculates the CDF of the Triangular distribution.
     */
    @Override
    public double cdf(double x, double... params) {
        if (params.length != 3)
            throw new IllegalArgumentException("Triangular requires a, b, c");
        double a = params[0];
        double b = params[1];
        double c = params[2];
        if (a >= b || c < a || c > b)
            throw new IllegalArgumentException("Invalid parameters: a <= c <= b");

        if (x <= a)
            return 0.0;
        if (x >= b)
            return 1.0;
        if (x <= c)
            return Math.pow(x - a, 2) / ((b - a) * (c - a));
        return 1.0 - Math.pow(b - x, 2) / ((b - a) * (b - c));
    }
}
