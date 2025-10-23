package com.simulation.prng.controllers;

import com.simulation.prng.controllers.tests.testsController;
import com.simulation.prng.utils.AlertHandler;
import com.simulation.prng.utils.ControllerStructure;
import com.simulation.prng.utils.Form;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
            executeButton,
            testsButton;

    @FXML
    public ListView<Double> listView;

    @FXML
    public Label lifeCycleLabel;

    private ControllerStructure controllerStructure;

    Consumer<ObservableList<Double>> success = (result) -> {
        listView.setItems(result);
        executeButton.setDisable(false);
        lifeCycleLabel.setText("Life Cicle: " + listView.getItems().size());
        testsController.results = result;
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
            if(controllerStructure != null) controllerStructure.execute();
        });

        clearButton.setOnAction((ActionEvent event) -> {
            if(controllerStructure != null) controllerStructure.clear();
        });

        testsButton.setOnAction((ActionEvent event) -> {
            if (controllerStructure != null) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(testsController.PATH));
                try {
                    Node node = fxmlLoader.load();
                    Scene scene = new Scene((Parent) node);

                    Stage stage = new Stage();
                    stage.setTitle("Tests");
                    stage.setScene(scene);

                    System.out.println("hola");
                    stage.showAndWait();
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

        lifeCycleLabel.setText("...");
    }

    public void loadPage(String path) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(path));
        Node node = fxmlLoader.load();
        controllerStructure = fxmlLoader.getController();
        controllerStructure.setSharedComponents(this.executeButton, this.success, this.failure);

        Node center = borderPane.getCenter();
        assert center instanceof VBox;
        VBox vbox = (VBox) center;
        vbox.getChildren().clear();
        vbox.getChildren().add(node);
    }

    public void saveData(ActionEvent actionEvent) {
    }
}

