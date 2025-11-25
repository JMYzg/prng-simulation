package com.simulation.prng.controllers.tests;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.net.URL;
import java.util.*;

public class testsController implements Initializable {

    public static String PATH = "/views/tests.fxml";
    public static ObservableList<Double> results;

    @FXML
    public Label
            meanLabel,
            varianceLabel,
            chiSquareLabel,
            runsLabel,
            runsLengthLabel,
            gapsLabel,
            pokerLabel;

    private static final double ALPHA = 0.05;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLabels();
    }

    public void updateLabels() {
        if (results == null || results.isEmpty()) {
            clearLabels();
            return;
        }

        double[] dataArray = results.stream().mapToDouble(Double::doubleValue).toArray();
        DescriptiveStatistics stats = new DescriptiveStatistics(dataArray);

        double mean = stats.getMean();
        double variance = stats.getVariance();

        meanLabel.setText(String.format("%.6f (Expected: 0.5)", mean));
        varianceLabel.setText(String.format("%.6f (Expected: 0.083)", variance));

        TestResult chiResult = calculateChiSquare(dataArray);
        chiSquareLabel.setText(String.format("χ²=%.4f, p=%.4f %s",
                chiResult.statistic, chiResult.pValue, chiResult.passed ? "✓" : "✗"));

        TestResult runsResult = calculateRunsTest(dataArray);
        runsLabel.setText(String.format("Z=%.4f, p=%.4f %s",
                runsResult.statistic, runsResult.pValue, runsResult.passed ? "✓" : "✗"));

        TestResult runsLengthResult = calculateRunsLengthTest(dataArray);
        runsLengthLabel.setText(String.format("Avg=%.4f (Expected: ~2) %s",
                runsLengthResult.statistic, runsLengthResult.passed ? "✓" : "✗"));

        TestResult gapsResult = calculateGapsTest(dataArray);
        gapsLabel.setText(String.format("χ²=%.4f, p=%.4f %s",
                gapsResult.statistic, gapsResult.pValue, gapsResult.passed ? "✓" : "✗"));

        TestResult pokerResult = calculatePokerTest(dataArray);
        pokerLabel.setText(String.format("χ²=%.4f, p=%.4f %s",
                pokerResult.statistic, pokerResult.pValue, pokerResult.passed ? "✓" : "✗"));
    }

    private void clearLabels() {
        meanLabel.setText("---");
        varianceLabel.setText("---");
        chiSquareLabel.setText("---");
        runsLabel.setText("---");
        runsLengthLabel.setText("---");
        gapsLabel.setText("---");
        pokerLabel.setText("---");
    }

    private static class TestResult {
        double statistic;
        double pValue;
        boolean passed;

        TestResult(double statistic, double pValue, boolean passed) {
            this.statistic = statistic;
            this.pValue = pValue;
            this.passed = passed;
        }
    }

    private static TestResult calculateChiSquare(double[] data) {
        int k = 10;
        int[] observed = new int[k];
        int n = data.length;
        double expected = (double) n / k;

        for (double value : data) {
            int bin = Math.min((int) (value * k), k - 1);
            observed[bin]++;
        }

        double chiSquare = 0.0;
        for (int i = 0; i < k; i++) {
            chiSquare += Math.pow(observed[i] - expected, 2) / expected;
        }

        int df = k - 1;
        ChiSquaredDistribution chiDist = new ChiSquaredDistribution(df);
        double pValue = 1.0 - chiDist.cumulativeProbability(chiSquare);
        boolean passed = pValue > ALPHA;

        return new TestResult(chiSquare, pValue, passed);
    }

    private static TestResult calculateRunsTest(double[] data) {
        int n = data.length;

        double median = StatUtils.percentile(data, 50.0);

        int n1 = 0;
        int n2 = 0;
        int runs = 1;

        boolean[] aboveMedian = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (data[i] >= median) {
                aboveMedian[i] = true;
                n1++;
            } else {
                aboveMedian[i] = false;
                n2++;
            }
        }

        for (int i = 1; i < n; i++) {
            if (aboveMedian[i] != aboveMedian[i - 1]) {
                runs++;
            }
        }

        if (n1 == 0 || n2 == 0) {
            return new TestResult(0.0, 1.0, false);
        }

        double expectedRuns = (2.0 * n1 * n2) / (n1 + n2) + 1;
        double variance = (2.0 * n1 * n2 * (2.0 * n1 * n2 - n1 - n2)) /
                (Math.pow(n1 + n2, 2) * (n1 + n2 - 1));
        double stdDev = Math.sqrt(variance);

        double zScore = (runs - expectedRuns) / stdDev;

        NormalDistribution normalDist = new NormalDistribution();
        double pValue = 2.0 * (1.0 - normalDist.cumulativeProbability(Math.abs(zScore)));
        boolean passed = pValue > ALPHA;

        return new TestResult(zScore, pValue, passed);
    }

    private static TestResult calculateRunsLengthTest(double[] data) {
        double median = StatUtils.percentile(data, 50.0);

        List<Integer> runLengths = getIntegers(data, median);

        double[] lengths = runLengths.stream().mapToDouble(Integer::doubleValue).toArray();
        DescriptiveStatistics stats = new DescriptiveStatistics(lengths);
        double avgLength = stats.getMean();
        stats.getStandardDeviation();

        double expectedLength = 2.0;

        boolean passed = Math.abs(avgLength - expectedLength) < 0.5;

        return new TestResult(avgLength, 0.0, passed);
    }

    private static List<Integer> getIntegers(double[] data, double median) {
        List<Integer> runLengths = new ArrayList<>();
        int currentLength = 1;
        boolean currentAbove = data[0] >= median;

        for (int i = 1; i < data.length; i++) {
            boolean isAbove = data[i] >= median;
            if (isAbove == currentAbove) {
                currentLength++;
            } else {
                runLengths.add(currentLength);
                currentLength = 1;
                currentAbove = isAbove;
            }
        }
        runLengths.add(currentLength);
        return runLengths;
    }

    private static TestResult calculateGapsTest(double[] data) {
        double alpha = 0.0;
        double beta = 0.5;
        double p = beta - alpha;

        List<Integer> gaps = new ArrayList<>();
        int currentGap = -1;

        for (double value : data) {
            if (value >= alpha && value < beta) {
                if (currentGap >= 0) {
                    gaps.add(currentGap);
                }
                currentGap = 0;
            } else if (currentGap >= 0) {
                currentGap++;
            }
        }

        if (gaps.isEmpty() || gaps.size() < 10) {
            return new TestResult(0.0, 0.0, false);
        }

        int maxGap = 5;
        int[] observed = new int[maxGap + 1];

        for (int gap : gaps) {
            if (gap >= maxGap) {
                observed[maxGap]++;
            } else {
                observed[gap]++;
            }
        }

        int n = gaps.size();
        double chiSquare = 0.0;
        int validCategories = 0;

        for (int i = 0; i <= maxGap; i++) {
            double expected;
            if (i < maxGap) {
                expected = n * Math.pow(1 - p, i) * p;
            } else {
                expected = n * Math.pow(1 - p, maxGap);
            }

            if (expected >= 5) {
                chiSquare += Math.pow(observed[i] - expected, 2) / expected;
                validCategories++;
            }
        }

        return getTestResult(chiSquare, validCategories);
    }

    private static TestResult getTestResult(double chiSquare, int validCategories) {
        if (validCategories < 2) {
            return new TestResult(0.0, 0.0, false);
        }

        int df = validCategories - 1;

        ChiSquaredDistribution chiDist = new ChiSquaredDistribution(df);
        double pValue = 1.0 - chiDist.cumulativeProbability(chiSquare);
        boolean passed = pValue > ALPHA;

        return new TestResult(chiSquare, pValue, passed);
    }


    private static TestResult calculatePokerTest(double[] data) {
        int k = 5;
        int d = 10;

        int numGroups = data.length / k;

        // Validación: Se necesitan al menos 10 grupos para un test significativo
        if (numGroups < 10) {
            return new TestResult(0.0, 0.0, false);
        }

        int[] observed = new int[k + 1];

        for (int i = 0; i < numGroups; i++) {
            Set<Integer> distinctDigits = new HashSet<>();

            for (int j = 0; j < k; j++) {
                int idx = i * k + j;
                int digit = Math.min((int) (data[idx] * d), d - 1);
                distinctDigits.add(digit);
            }

            int r = distinctDigits.size();
            if (r >= 1 && r <= k) {
                observed[r]++;
            }
        }

        double chiSquare = 0.0;
        int validCategories = 0;

        for (int r = 1; r <= k; r++) {
            double pr = calculatePokerProbability(k, d, r);
            double expected = numGroups * pr;

            if (expected >= 5) {
                chiSquare += Math.pow(observed[r] - expected, 2) / expected;
                validCategories++;
            }
        }

        return getTestResult(chiSquare, validCategories);
    }


    private static double calculatePokerProbability(int k, int d, int r) {
        if (r > k || r > d) return 0.0;

        double numerator = 1.0;
        for (int i = 0; i < r; i++) {
            numerator *= (d - i);
        }

        double denominator = Math.pow(d, k);
        double stirling = stirlingSecondKind(k, r);

        return (numerator / denominator) * stirling;
    }

    private static double stirlingSecondKind(int n, int k) {
        if (n == 0 && k == 0) return 1.0;
        if (n == 0 || k == 0 || k > n) return 0.0;
        if (k == n) return 1.0;

        double sum = 0.0;
        for (int j = 0; j <= k; j++) {
            double sign = Math.pow(-1, k - j);
            double binomial = binomialCoefficient(k, j);
            double power = Math.pow(j, n);
            sum += sign * binomial * power;
        }

        return sum / factorial(k);
    }

    private static double binomialCoefficient(int n, int k) {
        if (k > n) return 0.0;
        if (k == 0 || k == n) return 1.0;

        k = Math.min(k, n - k);

        double result = 1.0;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= (i + 1);
        }

        return result;
    }

    private static double factorial(int n) {
        if (n <= 1) return 1.0;
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}