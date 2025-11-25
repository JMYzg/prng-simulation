package com.simulation.prng.controllers.algorithms;

import com.simulation.prng.models.QCG;
import com.simulation.prng.utils.templates.AlgorithmTemplate;
import com.simulation.prng.utils.templates.ControllerTemplate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Callable;

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
        return List.of(seedQCGTextField, aQCGTextField, bQCGTextField, cQCGTextField, modulusQCGTextField);
    }

    @Override
    protected Callable<ObservableList<Double>> createLogic(List<Long> parsedValues) {
        long seed = parsedValues.get(0);
        long a = parsedValues.get(1);
        long b = parsedValues.get(2);
        long c = parsedValues.get(3);
        long modulus = parsedValues.get(4);
        return () -> {
            AlgorithmTemplate qcg = new QCG(seed, a, b, c, modulus);
            return FXCollections.observableArrayList(qcg.generate());
        };
    }
}