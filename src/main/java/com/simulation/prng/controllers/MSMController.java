package com.simulation.prng.controllers;

import com.simulation.prng.models.MSM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.List;

public class MSMController {
    @FXML
    public TextField
            seedMSMTextField,
            iterationMSMTextField;

    public ObservableList<Long> HandleInputs() {

//        long seed;
//        int iterations;
//
//        try {
//            long seed = Long.parseLong(seedMSMTextField.getText().trim());
//            int iterations = Integer.parseInt(iterationMSMTextField.getText().trim());
//        } catch (NumberFormatException e) {
//            AlertHandler.showAlert(
//                    Alert.AlertType.ERROR,
//
//            )
//        }
//        return FXCollections.observableArrayList(MSM.generate(seed, iterations));
        return FXCollections.observableArrayList();
    }
}
