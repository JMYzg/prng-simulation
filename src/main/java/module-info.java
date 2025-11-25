module com.simulation.prng {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires javafx.base;
    requires commons.math3;
    requires jdk.compiler;

    opens com.simulation.prng to javafx.fxml;
    exports com.simulation.prng;

    exports com.simulation.prng.utils;
    opens com.simulation.prng.utils to javafx.fxml;

    exports com.simulation.prng.controllers;
    opens com.simulation.prng.controllers to javafx.fxml;

    exports com.simulation.prng.controllers.algorithms;
    opens com.simulation.prng.controllers.algorithms to javafx.fxml;

    exports com.simulation.prng.controllers.tests;
    opens com.simulation.prng.controllers.tests to javafx.fxml;

    exports com.simulation.prng.utils.templates;
    opens com.simulation.prng.utils.templates to javafx.fxml;
}