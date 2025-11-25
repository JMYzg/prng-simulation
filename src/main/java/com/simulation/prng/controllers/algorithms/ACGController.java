package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.ACG;
import com.simulation.prng.utils.AlertHandler;
import com.simulation.prng.utils.ControllerStructure;
import com.simulation.prng.utils.TaskFactory;
import com.simulation.prng.utils.Validator;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class ACGController implements ControllerStructure {

    @FXML
    public TextField
            seedsACGTextField,
            modulusACGTextField;

    private Button executeButton;
    private Consumer<ObservableList<Double>> success;
    private Consumer<Throwable> failure;

    @Override
    public void setSharedComponents(Button executeButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure) {
        this.executeButton = executeButton;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public void execute() {
        String seedsText = seedsACGTextField.getText().trim();
        String modulusText = modulusACGTextField.getText().trim();

        if (Validator.isEmpty(seedsText, modulusText)) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Empty fields",
                    "Please fill all the required fields."
            );
            return;
        }

        List<Long> seeds = new ArrayList<>(Objects.requireNonNull(Validator.parseCSVFormat(seedsText)));
        System.out.println(seeds);
        long modulus = Long.parseLong(modulusText);

        if (seeds.isEmpty()) return;
        if (Validator.isNotNatural(modulus)) {
            AlertHandler.showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Fields may not be natural numbers",
                    "Please make sure that all fields are natural numbers.\n" +
                            "Natural numbers are integers larger than zero."
            );
            return;
        }

        Callable<ObservableList<Double>> logic = () -> FXCollections.observableArrayList(ACG.generate(seeds, modulus));
        Task<ObservableList<Double>> task = TaskFactory.create(logic, success, failure);

        executeButton.setDisable(true);
        new Thread(task).start();
    }

    @Override
    public void clear() {
        seedsACGTextField.clear();
        modulusACGTextField.clear();
    }
}
