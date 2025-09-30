package com.simulation.prng.utils.templates;

import com.simulation.prng.utils.AlertHandler;
import com.simulation.prng.utils.ControllerStructure;
import com.simulation.prng.utils.TaskFactory;
import com.simulation.prng.utils.Validator;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public abstract class ControllerTemplate implements ControllerStructure {

    private Button executeButton;
    private Consumer<ObservableList<Double>> success;
    private Consumer<Throwable> failure;

    protected abstract List<TextField> getTextFields();
    protected abstract Callable<ObservableList<Double>> createLogic(List<Long> parsedValues);

    @Override
    public void setSharedComponents(Button executeButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure) {
        this.executeButton = executeButton;
        this.success = success;
        this.failure = failure;
    }

    @Override
    public final void execute() {
        List<TextField> textFields = getTextFields();
        List<Long> parsedValues = new ArrayList<>();

        for (TextField textField : textFields) {
            if (Validator.isEmpty(textField.getText().trim())) {
                AlertHandler.showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Empty fields",
                        "Please fill all the required fields."
                );
                return;
            }
        }

        try {
            for (TextField textField : textFields) {
                long value = Long.parseLong(textField.getText().trim());

                if (Validator.isNotNatural(value)) {
                    AlertHandler.showAlert(
                            Alert.AlertType.WARNING,
                            "Warning",
                            "Fields may not be natural numbers",
                            "Please make sure that all fields are natural numbers.\n" +
                                    "Natural numbers are integers larger than zero."
                    );
                    return;
                }

                parsedValues.add(value);
            }
        } catch (NumberFormatException e) {
            AlertHandler.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Incorrect format",
                    "Please enter a valid number.\n" +
                            "Code error: " + e.getMessage()
            );
            return;
        }

        Callable<ObservableList<Double>> logic = createLogic(parsedValues);
        Task<ObservableList<Double>> task = TaskFactory.create(logic, success, failure);

        executeButton.setDisable(true);
        new Thread(task).start();
    }

    @Override
    public final void clear() {
        getTextFields().forEach(TextField::clear);
    }
}
