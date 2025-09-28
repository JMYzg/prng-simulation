package com.simulation.prng.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class menuController implements Initializable {
    @FXML
    public BorderPane borderPane;
    @FXML
    public Label menuTitleLabel;
    @FXML
    public ComboBox<String> comboBox;
    @FXML
    public Button cleanButton, saveDataButton;
    @FXML
    public ScrollPane scrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> options = FXCollections.observableArrayList(
                "Algoritmo de cuadrados medios",
                "Algorithm de productos medios",
                "Algoritmo de multiplicador constante",
                "Algoritmo lineal",
                "Algoritmo congruencial multiplicativo",
                "Algoritmo congruencial aditivo",
                "Algoritmos congruencial cuadratico",
                "Algoritmos Blum, Blum y Shub"
        );

        comboBox.setPromptText("Choose an option");
        comboBox.setItems(options);

        comboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue != null){
                System.out.println(newValue);
                try {
                    load(newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void load(String newValue) throws IOException {
        switch (newValue){
            case "Algoritmo de cuadrados medios":
                loadPage("/com/simulation/prng/MSM.fxml");
                break;
            case "Algorithm de productos medios":
                loadPage("/com/simulation/prng/MPM.fxml");
                break;
            case "Algoritmo de multiplicador constante":
                loadPage("/com/simulation/prng/CMM.fxml");
                break;
            case "Algoritmo lineal":
                loadPage("/com/simulation/prng/LCG.fxml");
                break;
            case "Algoritmo congruencial multiplicativo":
                loadPage("/com/simulation/prng/MCG.fxml");
                break;
            case "Algoritmo congruencial aditivo":
                loadPage("/com/simulation/prng/ACG.fxml");
                break;
            case "Algoritmos congruencial cuadratico":
                loadPage("/com/simulation/prng/QCG.fxml");
                break;
            case "Algoritmos Blum, Blum y Shub":
                loadPage("/com/simulation/prng/BBS.fxml");
                break;
            default:
                System.out.println("Error");
                break;
        }
    }

    public void loadPage(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(path));
        Node node = fxmlLoader.load();
        borderPane.setCenter(node);
    }

    public void clean(ActionEvent actionEvent) {
    }

    public void saveData(ActionEvent actionEvent) {
    }
}

