package com.simulation.prng.controllers;

import com.simulation.prng.controllers.utils.AlertHandler;
import com.simulation.prng.controllers.utils.Algorithm;
import com.simulation.prng.utils.Form;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public BorderPane borderPane;

    @FXML
    public ComboBox<Form> comboBox;

    @FXML
    public Button
            clearButton,
            saveDataButton,
            executeButton;

    @FXML
    public ListView<Long> listView;

    Algorithm algorithm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboBox.setPromptText("Choose an option");
        comboBox.getItems().setAll(Form.values());

        comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null){
                try {
                    loadPage(newValue.getFxml());
                } catch (IOException e) {
                    AlertHandler.showAlert(
                            Alert.AlertType.ERROR,
                            "Error",
                            "Unexpected error",
                            "An error occurred trying to load the fxml file\n" +
                                    "Code error: " + e.getMessage()
                    );
                }
            }
        });

        comboBox.getSelectionModel().select(0);

        executeButton.setOnAction((ActionEvent event) -> {
            if(algorithm != null) algorithm.execute();
        });
    }

    public void loadPage(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
//        fxmlLoader.setLocation(getClass().getResource(path));

        Node node = fxmlLoader.load();

        algorithm = fxmlLoader.getController();
        algorithm.setSharedComponents(this.listView, this.executeButton);

        borderPane.setCenter(node);
    }

    public void clean(ActionEvent actionEvent) {
    }

    public void saveData(ActionEvent actionEvent) {
    }
}

