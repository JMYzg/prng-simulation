package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.LCG;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class LCGController extends ControllerTemplate {

    @FXML
    public TextField
            seedLCGTextField,
            multiplierLCGTextField,
            incrementLCGTextField,
            modulusLCGTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedLCGTextField, multiplierLCGTextField, incrementLCGTextField, modulusLCGTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long multiplier = parsedValues.get(1);
        long increment = parsedValues.get(2);
        long modulus = parsedValues.get(3);
        return () -> {
            AlgorithmTemplate LCG = new LCG(seed, multiplier, increment, modulus);
            return FXCollections.observableArrayList(LCG.generate());
        };
    }
}
