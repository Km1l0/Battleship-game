package com.example.hellojavafx.models;

import javafx.scene.Parent;

/**
 * Clase que representa un barco en el juego de Batalla Naval.
 * Gestiona el tipo, orientación y estado del barco.
 */
public class Ship extends Parent {
    /** Tamaño del barco. */
    public int type;

    /** Indica si el barco está orientado verticalmente. */
    public boolean vertical = true;

    /** Salud actual del barco. */
    private int health;

    /**
     * Constructor de la clase Ship.
     *
     * @param type     Tipo o tamaño del barco (número de celdas).
     * @param vertical Indica si el barco está orientado verticalmente.
     */
    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        this.health = type;

        /*
         * Sección comentada que originalmente generaba un gráfico
         * para representar visualmente el barco.
         *
         * VBox vbox = new VBox();
         * for (int i = 0; i < type; i++) {
         *     Rectangle square = new Rectangle(30, 30);
         *     square.setFill(null);
         *     square.setStroke(Color.BLACK);
         *     vbox.getChildren().add(square);
         * }
         * getChildren().add(vbox);
         */
    }

    /**
     * Maneja un impacto en el barco, reduciendo su salud.
     */
    public void hit() {
        health--;
    }

    /**
     * Verifica si el barco sigue vivo (no ha sido destruido).
     *
     * @return Verdadero si el barco tiene salud restante, falso si no.
     */
    public boolean isAlive() {
        return health > 0;
    }
}
