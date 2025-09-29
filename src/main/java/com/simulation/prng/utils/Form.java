package com.simulation.prng.utils;

public enum Form {
    MSM("Cuadrados Medios", "/com/simulation/prng/MSM.fxml"),
    MPM("Productos Medios", "/com/simulation/prng/MPM.fxml"),
    CMM("Multiplicador Constante", "/com/simulation/prng/CMM.fxml"),
    LCG("Lineal", "/com/simulation/prng/LCG.fxml"),
    MCG("Congruencial Multiplicativo", "/com/simulation/prng/MCG.fxml"),
    ACG("Congruencial Aditivo", "/com/simulation/prng/ACG.fxml"),
    QCG("Congruencial Cuadrático", "/com/simulation/prng/QCG.fxml"),
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
