package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.ACG;
import com.simulation.prng.models.QCG;
import com.simulation.prng.utils.AlertHandler;
import com.simulation.prng.utils.ControllerStructure;
import com.simulation.prng.utils.TaskFactory;
import com.simulation.prng.utils.Validator;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
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

public class QCGController extends ControllerTemplate {

    @FXML
    public TextField
            seedQCGTextField,
            aQCGTextField,
            bQCGTextField,
            cQCGTextField,
            modulusQCGTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedQCGTextField, aQCGTextField, bQCGTextField, cQCGTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long a = parsedValues.get(1);
        long b = parsedValues.get(2);
        long c = parsedValues.get(3);
        long modulus = parsedValues.get(4);
        return () -> FXCollections.observableArrayList(QCG.generate(seed, a, b, c, modulus));
    }
}
