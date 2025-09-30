package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.CMM;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class CMMController extends ControllerTemplate {

    @FXML
    public TextField
            seedCMMTextField,
            constantCMMTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedCMMTextField, constantCMMTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long constant = parsedValues.get(1);
        return () -> {
            AlgorithmTemplate CMM = new CMM(seed, constant);
            return FXCollections.observableArrayList(CMM.generate());
        };
    }
}
