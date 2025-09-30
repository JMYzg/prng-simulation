package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.BBS;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

public class BBSController extends ControllerTemplate {

    @FXML
    public TextField
            seedBBSTextField,
            pBBSTextField,
            qBBSTextField;

    @Override
    protected List<TextField> getTextFields() {
        return List.of(seedBBSTextField, pBBSTextField, qBBSTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long p = parsedValues.get(1);
        long q = parsedValues.get(2);
        return () -> {
            AlgorithmTemplate BBS = new BBS(seed, p, q);
            return FXCollections.observableArrayList(BBS.generate());
        };
    }
}
