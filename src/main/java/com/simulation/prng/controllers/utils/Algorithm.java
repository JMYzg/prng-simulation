package com.simulation.prng.controllers.utils;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public interface Algorithm {

    void execute();
    void setSharedComponents(ListView<Long> listView, Button excecuteButton);
}
