package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.MSM;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class MSMController extends ControllerTemplate {

    @FXML
    private TextField seedMSMTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedMSMTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        return () -> {
            AlgorithmTemplate MSM = new MSM(seed);
            return FXCollections.observableArrayList(MSM.generate());
        };
    }
}
