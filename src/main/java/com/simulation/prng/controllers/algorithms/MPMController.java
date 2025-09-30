package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.MPM;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class MPMController extends ControllerTemplate {

    @FXML
    public TextField
            seed1MPMTextField,
            seed2MPMTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seed1MPMTextField, seed2MPMTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed1 = parsedValues.get(0);
        long seed2 = parsedValues.get(1);
        return () -> {
            AlgorithmTemplate MPM = new MPM(seed1, seed2);
            return FXCollections.observableArrayList(MPM.generate());
        };
    }
}
