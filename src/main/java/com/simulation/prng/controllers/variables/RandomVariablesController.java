package com.simulation.prng.controllers.variables;

import com.simulation.prng.models.variables.*;
import com.simulation.prng.utils.AlertHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controller for the Random Variables interface.
 * Refactored to support UI/UX improvements, statistical analysis, and strict
 * mathematical adherence.
 *
 * @author PRNG Simulation Team
 */
public class RandomVariablesController implements Initializable {

    // --- FXML Components ---

    @FXML
    private ComboBox<String> cbDistribution;
    @FXML
    private StackPane stackInputForms;

    // Input Panes
    @FXML
    private VBox paneUniform;
    @FXML
    private VBox paneExponential;
    @FXML
    private VBox paneNormal;
    @FXML
    private VBox paneErlang;
    @FXML
    private VBox paneBernoulli;
    @FXML
    private VBox panePoisson;
    @FXML
    private VBox paneBinomial;
    @FXML
    private VBox paneTriangular;

    // Input Fields
    @FXML
    private TextField txtMin;
    @FXML
    private TextField txtMax;
    @FXML
    private TextField txtLambda;
    @FXML
    private TextField txtMean;
    @FXML
    private TextField txtStDev;
    @FXML
    private TextField txtErlangK;
    @FXML
    private TextField txtErlangLambda;
    @FXML
    private TextField txtProb;
    @FXML
    private TextField txtPoissonLambda;
    @FXML
    private TextField txtBinomialN;
    @FXML
    private TextField txtBinomialP;
    @FXML
    private TextField txtTriA;
    @FXML
    private TextField txtTriB;
    @FXML
    private TextField txtTriC;

    // Results & Visualization
    @FXML
    private ListView<String> listResults;
    @FXML
    private BarChart<String, Number> chartHistogram;
    @FXML
    private Label lblMean;
    @FXML
    private Label lblVariance;
    @FXML
    private Label lblMode;

    // Actions
    @FXML
    private Button btnSimulate;
    @FXML
    private Button btnClear;

    // --- Constants & Data ---

    private static final String DIST_UNIFORM = "Uniform";
    private static final String DIST_EXPONENTIAL = "Exponential";
    private static final String DIST_NORMAL = "Normal";
    private static final String DIST_ERLANG = "Erlang";
    private static final String DIST_BERNOULLI = "Bernoulli";
    private static final String DIST_POISSON = "Poisson";
    private static final String DIST_BINOMIAL = "Binomial";
    private static final String DIST_TRIANGULAR = "Triangular";

    private List<Double> pseudoRandomNumbers;
    private final DecimalFormat df = new DecimalFormat("#.####");

    /**
     * Sets the list of pseudo-random numbers to be used for generation.
     */
    public void setPseudoRandomNumbers(List<Double> pseudoRandomNumbers) {
        this.pseudoRandomNumbers = pseudoRandomNumbers;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize ComboBox
        cbDistribution.getItems().addAll(
                DIST_UNIFORM, DIST_EXPONENTIAL, DIST_NORMAL, DIST_ERLANG,
                DIST_BERNOULLI, DIST_POISSON, DIST_BINOMIAL, DIST_TRIANGULAR);

        // Add Listener for Pane Switching
        cbDistribution.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showPane(newVal);
                clearResults(); // Clear previous results when switching
            }
        });

        // Button Actions
        btnSimulate.setOnAction(e -> onGenerate());
        btnClear.setOnAction(e -> onClear());

        // Auto-select first item
        cbDistribution.getSelectionModel().selectFirst();
    }

    private void showPane(String distribution) {
        // Hide all first
        paneUniform.setVisible(false);
        paneExponential.setVisible(false);
        paneNormal.setVisible(false);
        paneErlang.setVisible(false);
        paneBernoulli.setVisible(false);
        panePoisson.setVisible(false);
        paneBinomial.setVisible(false);
        paneTriangular.setVisible(false);

        switch (distribution) {
            case DIST_UNIFORM -> paneUniform.setVisible(true);
            case DIST_EXPONENTIAL -> paneExponential.setVisible(true);
            case DIST_NORMAL -> paneNormal.setVisible(true);
            case DIST_ERLANG -> paneErlang.setVisible(true);
            case DIST_BERNOULLI -> paneBernoulli.setVisible(true);
            case DIST_POISSON -> panePoisson.setVisible(true);
            case DIST_BINOMIAL -> paneBinomial.setVisible(true);
            case DIST_TRIANGULAR -> paneTriangular.setVisible(true);
        }
    }

    private void onGenerate() {
        String selectedDist = cbDistribution.getValue();
        if (selectedDist == null) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Warning", "No Distribution Selected",
                    "Please select a distribution type.");
            return;
        }

        if (pseudoRandomNumbers == null || pseudoRandomNumbers.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Error", "No PRNs Available",
                    "Please generate Pseudo-Random Numbers in the main window first.");
            return;
        }

        try {
            List<Double> generatedValues = new ArrayList<>();
            Iterator<Double> iterator = pseudoRandomNumbers.iterator();

            // Generation Loop: Consumes PRNs until exhausted or insufficient for next
            // variable
            while (iterator.hasNext()) {
                try {
                    double val = generateValue(selectedDist, iterator);
                    generatedValues.add(val);
                } catch (NoSuchElementException e) {
                    // PRNs exhausted mid-calculation for a variable (e.g. Normal needing 12 but
                    // only 5 left)
                    // Stop generation
                    break;
                }
            }

            if (generatedValues.isEmpty()) {
                AlertHandler.showAlert(Alert.AlertType.WARNING, "Warning", "Insufficient PRNs",
                        "Not enough random numbers to generate a single variable.");
                return;
            }

            displayResults(generatedValues);
            updateStats(generatedValues);
            updateChart(generatedValues, selectedDist);

        } catch (NumberFormatException e) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Number Format",
                    "Please check your input parameters.\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid Parameter", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Error", "Unexpected Error", e.getMessage());
        }
    }

    private double generateValue(String dist, Iterator<Double> iterator) {
        // Note: Iterator.next() throws NoSuchElementException if no elements remain.
        // This is caught in the loop to stop generation.

        switch (dist) {
            case DIST_UNIFORM:
                double min = Double.parseDouble(txtMin.getText());
                double max = Double.parseDouble(txtMax.getText());
                return new UniformDist().generate(iterator, min, max);

            case DIST_EXPONENTIAL:
                double lambdaExp = Double.parseDouble(txtLambda.getText());
                return new ExponentialDist().generate(iterator, lambdaExp);

            case DIST_NORMAL:
                double mean = Double.parseDouble(txtMean.getText());
                double stDev = Double.parseDouble(txtStDev.getText());
                return new NormalDist().generate(iterator, mean, stDev);

            case DIST_ERLANG:
                int k = Integer.parseInt(txtErlangK.getText());
                double lambdaErl = Double.parseDouble(txtErlangLambda.getText());
                return new ErlangDist().generate(iterator, (double) k, lambdaErl);

            case DIST_BERNOULLI:
                double pBern = Double.parseDouble(txtProb.getText());
                return new BernoulliDist().generate(iterator, pBern);

            case DIST_POISSON:
                double lambdaPois = Double.parseDouble(txtPoissonLambda.getText());
                return new PoissonDist().generate(iterator, lambdaPois);

            case DIST_BINOMIAL:
                int nBin = Integer.parseInt(txtBinomialN.getText());
                double pBin = Double.parseDouble(txtBinomialP.getText());
                return new BinomialDist().generate(iterator, (double) nBin, pBin);

            case DIST_TRIANGULAR:
                double minTri = Double.parseDouble(txtTriA.getText());
                double maxTri = Double.parseDouble(txtTriB.getText());
                double modeTri = Double.parseDouble(txtTriC.getText());
                return new TriangularDist().generate(iterator, minTri, maxTri, modeTri);

            default:
                throw new IllegalArgumentException("Unknown distribution");
        }
    }

    private void displayResults(List<Double> values) {
        List<String> formatted = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            formatted.add(String.format("%d. %s", i + 1, df.format(values.get(i))));
        }
        listResults.setItems(FXCollections.observableArrayList(formatted));
    }

    private void updateStats(List<Double> values) {
        if (values.isEmpty())
            return;

        // Mean
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        double mean = sum / values.size();
        lblMean.setText(df.format(mean));

        // Variance
        double variance = 0.0;
        if (values.size() > 1) {
            double sumDiffSq = 0.0;
            for (double v : values) {
                sumDiffSq += Math.pow(v - mean, 2);
            }
            variance = sumDiffSq / (values.size() - 1);
        }
        lblVariance.setText(df.format(variance));

        // Mode
        // For continuous values, exact mode is rare. We'll find the most frequent value
        // (if any) or bin.
        // For this assignment, finding the most frequent value (even if count is 1) is
        // the standard interpretation.
        Map<Double, Long> frequencyMap = values.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long maxFreq = Collections.max(frequencyMap.values());
        if (maxFreq > 1) {
            // Find all values with maxFreq
            List<Double> modes = frequencyMap.entrySet().stream()
                    .filter(e -> e.getValue() == maxFreq)
                    .map(Map.Entry::getKey)
                    .sorted()
                    .collect(Collectors.toList());

            if (modes.size() == 1) {
                lblMode.setText(df.format(modes.get(0)));
            } else {
                lblMode.setText("Multimodal"); // Or list first few
            }
        } else {
            lblMode.setText("N/A (Unique)");
        }
    }

    private void updateChart(List<Double> values, String distType) {
        chartHistogram.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Frequency");

        // Determine if discrete or continuous
        boolean isDiscrete = distType.equals(DIST_BERNOULLI) || distType.equals(DIST_POISSON)
                || distType.equals(DIST_BINOMIAL);

        if (isDiscrete) {
            // Group by integer value
            Map<Integer, Long> frequency = values.stream()
                    .map(Double::intValue)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            frequency.keySet().stream().sorted().forEach(key -> {
                series.getData().add(new XYChart.Data<>(String.valueOf(key), frequency.get(key)));
            });
        } else {
            // Binning for continuous
            int numBins = (int) Math.sqrt(values.size());
            if (numBins < 5)
                numBins = 5;
            if (numBins > 20)
                numBins = 20;

            double min = values.stream().min(Double::compareTo).orElse(0.0);
            double max = values.stream().max(Double::compareTo).orElse(1.0);
            double range = max - min;
            double binSize = range / numBins;

            // Handle case where all values are same (range = 0)
            if (range == 0) {
                series.getData().add(new XYChart.Data<>(df.format(min), values.size()));
            } else {
                int[] bins = new int[numBins];
                for (double v : values) {
                    int binIdx = (int) ((v - min) / binSize);
                    if (binIdx >= numBins)
                        binIdx = numBins - 1;
                    bins[binIdx]++;
                }

                for (int i = 0; i < numBins; i++) {
                    double binStart = min + (i * binSize);
                    double binEnd = binStart + binSize;
                    String label = String.format("[%s, %s)", df.format(binStart), df.format(binEnd));
                    series.getData().add(new XYChart.Data<>(label, bins[i]));
                }
            }
        }

        chartHistogram.getData().add(series);
    }

    private void onClear() {
        // Clear all text fields
        txtMin.clear();
        txtMax.clear();
        txtLambda.clear();
        txtMean.clear();
        txtStDev.clear();
        txtErlangK.clear();
        txtErlangLambda.clear();
        txtProb.clear();
        txtPoissonLambda.clear();
        txtBinomialN.clear();
        txtBinomialP.clear();
        txtTriA.clear();
        txtTriB.clear();
        txtTriC.clear();

        clearResults();
    }

    private void clearResults() {
        listResults.getItems().clear();
        chartHistogram.getData().clear();
        lblMean.setText("-");
        lblVariance.setText("-");
        lblMode.setText("-");
    }
}
