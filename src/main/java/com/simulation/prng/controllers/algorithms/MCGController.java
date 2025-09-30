package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.MCG;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class MCGController extends ControllerTemplate {

    @FXML
    public TextField
            seedMCGTextField,
            multiplierMCGTextField,
            modulusMCGTextField;


    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedMCGTextField, multiplierMCGTextField, modulusMCGTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long multiplier = parsedValues.get(1);
        long modulus = parsedValues.get(2);
        return () -> {
            AlgorithmTemplate MCG = new MCG(seed, multiplier, modulus);
            return FXCollections.observableArrayList(MCG.generate());
        };
    }
}
