package com.simulation.prng.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;

import java.util.function.Consumer;

public interface ControllerStructure {
    void execute();

    void clear();

    void setSharedComponents(Button executeButton, Consumer<ObservableList<Double>> success, Consumer<Throwable> failure);
}
