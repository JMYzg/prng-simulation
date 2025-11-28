package com.simulation.prng.models.variables.tests;

import com.simulation.prng.models.variables.RandomVariable;
import java.util.List;

/**
 * Manager class to execute Goodness of Fit tests.
 */
public class GoodnessTestManager {

    private final ChiSquareTest chiSquareTest;
    private final KolmogorovSmirnovTest ksTest;

    public GoodnessTestManager() {
        this.chiSquareTest = new ChiSquareTest();
        this.ksTest = new KolmogorovSmirnovTest();
    }

    /**
     * Runs the Chi-Square Goodness of Fit Test.
     * 
     * @param data         the generated random numbers
     * @param distribution the theoretical distribution model
     * @param params       distribution parameters
     * @return Result object containing statistic, critical value, and pass/fail
     *         status
     */
    public ChiSquareTest.Result runChiSquareTest(List<Double> data, RandomVariable distribution, double... params) {
        return chiSquareTest.runTest(data, distribution, params);
    }

    /**
     * Runs the Kolmogorov-Smirnov Goodness of Fit Test.
     * Only valid for continuous distributions.
     * 
     * @param data         the generated random numbers
     * @param distribution the theoretical distribution model
     * @param params       distribution parameters
     * @return Result object containing statistic, critical value, and pass/fail
     *         status
     * @throws IllegalArgumentException if distribution is not continuous
     */
    public KolmogorovSmirnovTest.Result runKolmogorovSmirnovTest(List<Double> data, RandomVariable distribution,
            double... params) {
        return ksTest.runTest(data, distribution, params);
    }
}
