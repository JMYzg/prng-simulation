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
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

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
    private Button btnTest;
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
    private List<Double> generatedVariablesList;
    private RandomVariable currentModel;
    private double[] currentParams;
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
        btnTest.setOnAction(e -> onTest());
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
            // Instantiate model and params once
            prepareModel(selectedDist);

            generatedVariablesList = new ArrayList<>();
            Iterator<Double> iterator = pseudoRandomNumbers.iterator();

            // Generation Loop
            while (iterator.hasNext()) {
                try {
                    double val = currentModel.generate(iterator, currentParams);
                    generatedVariablesList.add(val);
                } catch (NoSuchElementException e) {
                    break;
                }
            }

            if (generatedVariablesList.isEmpty()) {
                AlertHandler.showAlert(Alert.AlertType.WARNING, "Warning", "Insufficient PRNs",
                        "Not enough random numbers to generate a single variable.");
                return;
            }

            displayResults(generatedVariablesList);
            updateStats(generatedVariablesList);
            updateChart(generatedVariablesList, selectedDist);

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

    private void prepareModel(String dist) {
        switch (dist) {
            case DIST_UNIFORM:
                double min = Double.parseDouble(txtMin.getText());
                double max = Double.parseDouble(txtMax.getText());
                currentModel = new UniformDist();
                currentParams = new double[] { min, max };
                break;

            case DIST_EXPONENTIAL:
                double lambdaExp = Double.parseDouble(txtLambda.getText());
                currentModel = new ExponentialDist();
                currentParams = new double[] { lambdaExp };
                break;

            case DIST_NORMAL:
                double mean = Double.parseDouble(txtMean.getText());
                double stDev = Double.parseDouble(txtStDev.getText());
                currentModel = new NormalDist();
                currentParams = new double[] { mean, stDev };
                break;

            case DIST_ERLANG:
                int k = Integer.parseInt(txtErlangK.getText());
                double lambdaErl = Double.parseDouble(txtErlangLambda.getText());
                currentModel = new ErlangDist();
                currentParams = new double[] { k, lambdaErl };
                break;

            case DIST_BERNOULLI:
                double pBern = Double.parseDouble(txtProb.getText());
                currentModel = new BernoulliDist();
                currentParams = new double[] { pBern };
                break;

            case DIST_POISSON:
                double lambdaPois = Double.parseDouble(txtPoissonLambda.getText());
                currentModel = new PoissonDist();
                currentParams = new double[] { lambdaPois };
                break;

            case DIST_BINOMIAL:
                int nBin = Integer.parseInt(txtBinomialN.getText());
                double pBin = Double.parseDouble(txtBinomialP.getText());
                currentModel = new BinomialDist();
                currentParams = new double[] { nBin, pBin };
                break;

            case DIST_TRIANGULAR:
                double minTri = Double.parseDouble(txtTriA.getText());
                double maxTri = Double.parseDouble(txtTriB.getText());
                double modeTri = Double.parseDouble(txtTriC.getText());
                currentModel = new TriangularDist();
                currentParams = new double[] { minTri, maxTri, modeTri };
                break;

            default:
                throw new IllegalArgumentException("Unknown distribution");
        }
    }

    private void onTest() {
        if (generatedVariablesList == null || generatedVariablesList.isEmpty()) {
            AlertHandler.showAlert(Alert.AlertType.WARNING, "Warning", "No Data",
                    "Please generate variables first.");
            return;
        }

        try {
            if (currentModel == null) {
                AlertHandler.showAlert(Alert.AlertType.ERROR, "Error", "No Model", "Model state is missing.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GoodnessOfFit.fxml"));
            Parent root = loader.load();
            GoodnessOfFitController controller = loader.getController();

            controller.initData(generatedVariablesList, currentModel, currentParams);

            Stage stage = new Stage();
            stage.setTitle("Goodness of Fit Test");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnTest.getScene().getWindow());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            AlertHandler.showAlert(Alert.AlertType.ERROR, "Error", "Could not open test window", e.getMessage());
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
