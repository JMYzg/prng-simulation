package com.simulation.prng.controllers;

import com.simulation.prng.controllers.utils.AlertHandler;
import com.simulation.prng.controllers.utils.Algorithm;
import com.simulation.prng.utils.Form;
import javafx.collections.ObservableList;
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
import java.util.function.Consumer;

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
    public ListView<Double> listView;

    private Algorithm algorithm;

    Consumer<ObservableList<Double>> success = (result) -> {
        listView.setItems(result);
        executeButton.setDisable(false);
    };

    Consumer<Throwable> failure = (exception) -> {
        AlertHandler.showAlert(
                Alert.AlertType.ERROR,
                "Error",
                "Unexpected Error",
                "An error occurred while executing the algorithm\n" +
                        "Code error: " + exception.getMessage()

        );
        executeButton.setDisable(false);
    };

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

        clearButton.setOnAction((ActionEvent event) -> {
            if(algorithm != null) algorithm.clear();
        });
    }

    public void loadPage(String path) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        Node node = fxmlLoader.load();

//        algorithm = fxmlLoader.getController();
//        algorithm.setSharedComponents(this.executeButton, this.success, this.failure);

        borderPane.setCenter(node);
    }

    public void saveData(ActionEvent actionEvent) {
    }
}

