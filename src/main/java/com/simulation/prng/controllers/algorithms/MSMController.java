package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.controllers.utils.AlertHandler;
import com.simulation.prng.controllers.utils.Algorithm;
import com.simulation.prng.models.MSM;
import com.simulation.prng.utils.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MSMController implements Algorithm {

    @FXML
    private TextField
            seedMSMTextField,
            iterationMSMTextField;

    private ListView<Long> listView;
    private Button executeButton;

    @Override
    public void setSharedComponents(ListView<Long> listView, Button excecuteButton) {
        this.listView = listView;
        this.executeButton = excecuteButton;
    }

    @Override
    public void execute() {

        String seedText = seedMSMTextField.getText().trim();
        String iterationText = iterationMSMTextField.getText().trim();

        if (seedText.isEmpty() || iterationText.isEmpty()) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Empty fields",
                    "Please fill all the required fields"
            );
            return;
        }

        long seed;
        int iterations;

        try {

            seed = Long.parseLong(seedText);
            iterations = Integer.parseInt(iterationText);

            if (!Validator.isNatural(seed) || !Validator.isNatural(iterations)) {
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

        Task<ObservableList<Long>> task = setTask(seed, iterations);

        executeButton.setDisable(true);
        new Thread(task).start();
    }

    private Task<ObservableList<Long>> setTask(long seed, int iterations) {
        Task<ObservableList<Long>> task = new Task<>() {
            @Override
            protected ObservableList<Long> call() throws Exception {
                return FXCollections.observableArrayList(MSM.generate(seed, iterations));
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
