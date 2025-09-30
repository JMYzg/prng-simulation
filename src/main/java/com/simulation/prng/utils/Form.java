package com.simulation.prng.utils;

public enum Form {
    MSM("Cuadrados Medios", "/views/MSM.fxml"),
    MPM("Productos Medios", "/views/MPM.fxml"),
    CMM("Multiplicador Constante", "/views/CMM.fxml"),
    LCG("Lineal", "/views/LCG.fxml"),
    MCG("Congruencial Multiplicativo", "/views/MCG.fxml"),
    ACG("Congruencial Aditivo", "/views/ACG.fxml"),
    QCG("Congruencial Cuadrático", "/views/QCG.fxml"),
    BBS("Blum, Blum y Shub", "/com/simulation/prng/BSS.fxml");

    final String title;
    final String fxml;

    Form(String algorithm, String fxml) {
        this.title = algorithm;
        this.fxml = fxml;
    }

    public String getFxml() {
        return fxml;
    }

    @Override
    public String toString() {
        return title;
    }
}
