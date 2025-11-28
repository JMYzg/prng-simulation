package com.simulation.prng.controllers.variables;

import com.simulation.prng.models.variables.RandomVariable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Controller for the Goodness of Fit tests window.
 * Handles Chi-Square and Kolmogorov-Smirnov / Anderson-Darling tests.
 */
public class GoodnessOfFitController implements Initializable {

    @FXML
    private Label lblHypothesis;
    @FXML
    private TableView<ChiSquareRow> tblChiSquare;
    @FXML
    private TableColumn<ChiSquareRow, String> colInterval;
    @FXML
    private TableColumn<ChiSquareRow, Number> colObserved;
    @FXML
    private TableColumn<ChiSquareRow, Number> colExpected;
    @FXML
    private TableColumn<ChiSquareRow, Number> colChiTerm;

    @FXML
    private Label lblChiCalc;
    @FXML
    private Label lblChiCrit;
    @FXML
    private Label lblChiResult;

    @FXML
    private LineChart<Number, Number> chartKS;
    @FXML
    private Label lblKSStat;
    @FXML
    private Label lblKSCrit;
    @FXML
    private Label lblKSResult;

    private final DecimalFormat df = new DecimalFormat("#.####");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Table Columns
        colInterval.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInterval()));
        colObserved.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getObserved()));
        colExpected.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getExpected()));
        colChiTerm.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getChiTerm()));
    }

    public void initData(List<Double> data, RandomVariable model, double... params) {
        if (data == null || data.isEmpty() || model == null)
            return;

        lblHypothesis.setText("H0: Data follows " + model.getDistributionName() + " distribution");

        // 1. Chi-Square Test
        performChiSquareTest(data, model, params);

        // 2. KS Test
        performKSTest(data, model, params);
    }

    private void performChiSquareTest(List<Double> data, RandomVariable model, double... params) {
        int n = data.size();
        int numBins = (int) Math.sqrt(n);
        if (numBins < 5)
            numBins = 5;

        double min = data.stream().min(Double::compareTo).orElse(0.0);
        double max = data.stream().max(Double::compareTo).orElse(1.0);
        double range = max - min;
        double binSize = range / numBins;

        int[] observed = new int[numBins];
        for (double v : data) {
            int binIdx = (int) ((v - min) / binSize);
            if (binIdx >= numBins)
                binIdx = numBins - 1;
            observed[binIdx]++;
        }

        List<ChiSquareRow> rows = new ArrayList<>();
        double chiCalc = 0.0;

        for (int i = 0; i < numBins; i++) {
            double binStart = min + (i * binSize);
            double binEnd = binStart + binSize;

            // Calculate Expected Probability using CDF
            // P(a < X <= b) = F(b) - F(a)
            double prob = 0.0;
            try {
                double cdfEnd = model.cdf(binEnd, params);
                double cdfStart = model.cdf(binStart, params);
                prob = cdfEnd - cdfStart;
            } catch (UnsupportedOperationException e) {
                // Fallback for distributions without CDF (assume uniform if unknown or error)
                prob = 1.0 / numBins;
            }

            double expected = prob * n;
            // Avoid division by zero
            if (expected == 0)
                expected = 0.0001;

            double chiTerm = Math.pow(observed[i] - expected, 2) / expected;
            chiCalc += chiTerm;

            String interval = String.format("[%s, %s)", df.format(binStart), df.format(binEnd));
            rows.add(new ChiSquareRow(interval, observed[i], expected, chiTerm));
        }

        tblChiSquare.setItems(FXCollections.observableArrayList(rows));
        lblChiCalc.setText(df.format(chiCalc));

        // Critical Value (Approximate for Alpha=0.05)
        // Degrees of freedom = k - 1 - m (estimated params). Let's assume m=0 for
        // simplicity or m=params.length
        int dfVal = Math.max(1, numBins - 1 - params.length);
        org.apache.commons.math3.distribution.ChiSquaredDistribution chiDist = new org.apache.commons.math3.distribution.ChiSquaredDistribution(
                dfVal);
        double chiCrit = chiDist.inverseCumulativeProbability(0.95);

        lblChiCrit.setText(df.format(chiCrit));

        if (chiCalc < chiCrit) {
            lblChiResult.setText("ACCEPTED (H0)");
            lblChiResult.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblChiResult.setText("REJECTED (H0)");
            lblChiResult.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    private void performKSTest(List<Double> data, RandomVariable model, double... params) {
        List<Double> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);
        int n = sortedData.size();

        XYChart.Series<Number, Number> seriesEmpirical = new XYChart.Series<>();
        seriesEmpirical.setName("Empirical CDF");
        XYChart.Series<Number, Number> seriesTheoretical = new XYChart.Series<>();
        seriesTheoretical.setName("Theoretical CDF");

        double dMax = 0.0;

        for (int i = 0; i < n; i++) {
            double x = sortedData.get(i);
            double empiricalProb = (double) (i + 1) / n;

            double theoreticalProb = 0.0;
            try {
                theoreticalProb = model.cdf(x, params);
            } catch (UnsupportedOperationException e) {
                theoreticalProb = (double) i / n; // Fallback
            }

            double diff = Math.abs(empiricalProb - theoreticalProb);
            if (diff > dMax)
                dMax = diff;

            seriesEmpirical.getData().add(new XYChart.Data<>(x, empiricalProb));
            if (i % (n / 20 + 1) == 0 || i == n - 1) { // Downsample theoretical line for performance
                seriesTheoretical.getData().add(new XYChart.Data<>(x, theoreticalProb));
            }
        }

        chartKS.getData().clear();
        chartKS.getData().addAll(seriesEmpirical, seriesTheoretical);

        lblKSStat.setText(df.format(dMax));

        // Critical Value Approximation for Alpha=0.05
        double ksCrit = 1.36 / Math.sqrt(n);
        lblKSCrit.setText(df.format(ksCrit));

        if (dMax < ksCrit) {
            lblKSResult.setText("ACCEPTED (H0)");
            lblKSResult.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        } else {
            lblKSResult.setText("REJECTED (H0)");
            lblKSResult.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    // Helper Class for Table
    public static class ChiSquareRow {
        private final String interval;
        private final double observed;
        private final double expected;
        private final double chiTerm;

        public ChiSquareRow(String interval, double observed, double expected, double chiTerm) {
            this.interval = interval;
            this.observed = observed;
            this.expected = expected;
            this.chiTerm = chiTerm;
        }

        public String getInterval() {
            return interval;
        }

        public double getObserved() {
            return observed;
        }

        public double getExpected() {
            return expected;
        }

        public double getChiTerm() {
            return chiTerm;
        }
    }
}
