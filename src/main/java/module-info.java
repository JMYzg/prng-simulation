module com.simulation.prng {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.graphics;
    requires javafx.base;

    opens com.simulation.prng to javafx.fxml;
    exports com.simulation.prng;

    exports com.simulation.prng.utils;
    opens com.simulation.prng.utils to javafx.fxml;

    exports com.simulation.prng.controllers;
    opens com.simulation.prng.controllers to javafx.fxml;
}