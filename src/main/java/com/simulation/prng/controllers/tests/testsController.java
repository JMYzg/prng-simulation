package com.simulation.prng.controllers.tests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class testsController {

    public static String PATH = "/views/tests.fxml";
    public static ObservableList<Double> results;

    @FXML
    public Label
            meanLabel,
            varianceLabel,
            chiSquareLabel,
            runsLabel,
            runsLengthLabel,
            gapsLabel,
            pokerLabel;
}
