package com.simulation.prng.controllers.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.function.Consumer;

public interface Algorithm {

    void execute();
    void clear();
    void setSharedComponents(Button executeButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure);
}
