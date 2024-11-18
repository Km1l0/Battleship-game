package com.example.hellojavafx.models;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un barco en el juego de Batalla Naval.
 * Gestiona el tipo, orientación y estado del barco.
 */
public class Ship extends Parent {
    /** Tamaño del barco (número de casillas que ocupa en el tablero). */
    public int type;

    /** Indica si el barco está orientado verticalmente. */
    public boolean vertical;

    /** Salud actual del barco (número de impactos que puede recibir antes de ser destruido). */
    private int health;

    /** Lista de celdas que ocupa el barco. */
    private final List<Rectangle> shipCells = new ArrayList<>();

    /**
     * Constructor de la clase Ship.
     *
     * @param type     Tipo o tamaño del barco (número de celdas que ocupa).
     * @param vertical Indica si el barco está orientado verticalmente (de ser false, se orienta horizontalmente).
     */
    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        this.health = type;

        // Crear las representaciones gráficas del barco
        if (vertical) {
            VBox vbox = new VBox();
            for (int i = 0; i < type; i++) {
                Rectangle square = new Rectangle(30, 30);
                square.setFill(Color.LIGHTGRAY); // Color para representar el barco
                square.setStroke(Color.BLACK);   // Bordes del barco
                vbox.getChildren().add(square);
                shipCells.add(square);
            }
            getChildren().add(vbox);  // Añadir el VBox al nodo principal
        } else {
            HBox hbox = new HBox();
            for (int i = 0; i < type; i++) {
                Rectangle square = new Rectangle(30, 30);
                square.setFill(Color.LIGHTGRAY); // Color para representar el barco
                square.setStroke(Color.BLACK);   // Bordes del barco
                hbox.getChildren().add(square);
                shipCells.add(square);
            }
            getChildren().add(hbox);  // Añadir el HBox al nodo principal
        }
    }

    /**
     * Maneja un impacto en el barco, reduciendo su salud.
     * Cambia el color de la celda del barco a rojo para indicar un impacto.
     */
    public void hit() {
        health--;
        // Cambiar el color de la celda que fue golpeada
        if (health < type) {
            shipCells.get(type - health - 1).setFill(Color.RED); // Cambiar el color de la última celda dañada
        }
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
