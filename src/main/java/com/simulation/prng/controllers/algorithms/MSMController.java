package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.controllers.utils.AlertHandler;
import com.simulation.prng.controllers.utils.Algorithm;
import com.simulation.prng.controllers.utils.TaskFactory;
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

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class MSMController implements Algorithm {

    @FXML
    private TextField
            seedMSMTextField,
            iterationMSMTextField;

    private Button executeButton;

    private Consumer<ObservableList<Double>> success;
    private Consumer<Throwable> failure;

    @Override
    public void setSharedComponents(Button excecuteButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure) {
        this.executeButton = excecuteButton;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public void execute() {

        String seedText = seedMSMTextField.getText().trim();
        String iterationText = iterationMSMTextField.getText().trim();

        if (Validator.isEmpty(seedText, iterationText)) {
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

            if (Validator.isNotNatural(seed) || Validator.isNotNatural(iterations)) {
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

        Callable<ObservableList<Double>> logic = () -> FXCollections.observableArrayList(MSM.generate(seed, iterations));
        Task<ObservableList<Double>> task = TaskFactory.create(logic, success, failure);

        executeButton.setDisable(true);
        new Thread(task).start();
    }

    @Override
    public void clear() {
        seedMSMTextField.clear();
        iterationMSMTextField.clear();
    }
}
