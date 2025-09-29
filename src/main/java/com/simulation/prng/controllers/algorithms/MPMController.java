package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.controllers.utils.AlertHandler;
import com.simulation.prng.controllers.utils.Algorithm;
import com.simulation.prng.models.MPM;
import com.simulation.prng.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class MPMController implements Algorithm {

    @FXML
    public TextField
            seed1MPMTextField,
            seed2MPMTextField,
            iterationMPMTextField;

    private ListView<Double> listView;
    private Button executeButton;
    private Consumer<ObservableList<Double>> success;
    private Consumer<Throwable> failure;

    @Override
    public void setSharedComponents(Button excecuteButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure) {
        this.listView = listView;
        this.executeButton = excecuteButton;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public void execute() {

        String seed1Text = seed1MPMTextField.getText().trim();
        String seed2Text = seed2MPMTextField.getText().trim();
        String iterationText = iterationMPMTextField.getText().trim();

        if (Validator.isEmpty(seed1Text, seed2Text, iterationText)) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Empty fields",
                    "Please fill all the required fields"
            );
            return;
        }

        long seed1;
        long seed2;
        int iterations;

        try {

            seed1 = Long.parseLong(seed1Text);
            seed2 = Long.parseLong(seed2Text);
            iterations = Integer.parseInt(iterationText);

            if (Validator.isNotNatural(seed1, seed2, iterations)) {
                AlertHandler.showAlert(
                        Alert.AlertType.WARNING,
                        "Warning",
                        "Fields may not be natural numbers",
                        "Please make sure that all fields are natural numbers\n" +
                                "Natural numbers are integers larger than zero."
                );
                return;
            }
        } catch (NumberFormatException e) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Incorrect format",
                    "Please enter a valid number\n" +
                            "Code error: " + e.getMessage()
            );
            return;
        }

        Task<ObservableList<Double>> task = new Task<>() {
            @Override
            protected ObservableList<Double> call() {
                return FXCollections.observableArrayList(MPM.generate(seed1, seed2, iterations));
            }
        };



        executeButton.setDisable(true);
        new Thread(task).start();
    }

    @Override
    public void clear() {
        seed1MPMTextField.clear();
        seed2MPMTextField.clear();
        iterationMPMTextField.clear();
    }

    private Task<ObservableList<Double>> setTask(long seed1, long seed2, int iterations) {
        Task<ObservableList<Double>> task = new Task<>() {
            @Override
            protected ObservableList<Double> call() {
                return FXCollections.observableArrayList(MPM.generate(seed1, seed2, iterations));
            }
        };

        task.setOnSucceeded(event -> {
            listView.setItems(task.getValue());
            executeButton.setDisable(false);
        });

        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unexpected Error",
                    "An error occurred while executing the algorithm\n" +
                            "Code error: " + exception.getMessage()

            );
            executeButton.setDisable(false);
        });
        return task;
    }
}
