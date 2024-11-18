package com.example.hellojavafx.controllers;

import com.example.hellojavafx.models.Ship;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa el tablero del juego de Batalla Naval.
 * Gestiona la disposición de las celdas, la colocación de barcos y las interacciones del jugador y el enemigo.
 */
public class Board extends Parent {
    /** Celda estática para operaciones compartidas. */
    public static Cell cell;

    /** Contenedor para las filas del tablero. */
    private VBox rows = new VBox();

    /** Indica si este tablero pertenece al enemigo. */
    private boolean enemy = false;

    /** Número de barcos restantes en el tablero. */
    public int ships = 10;

    /**
     * Constructor de la clase Board.
     *
     * @param enemy   Indica si el tablero es del enemigo.
     * @param handler Manejador de eventos para las interacciones con las celdas.
     */
    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.enemy = enemy;
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                // Asignar el manejador de eventos de mouse a cada celda
                c.setOnMouseClicked(handler);  // Esto debería funcionar si Cell extiende Rectangle
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    /**
     * Intenta colocar un barco en el tablero.
     *
     * @param ship El barco a colocar.
     * @param x    Coordenada X de inicio.
     * @param y    Coordenada Y de inicio.
     * @return Verdadero si el barco fue colocado exitosamente, falso de lo contrario.
     */
    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;

            // Previsualización del barco (puede ser modificada si lo deseas)
            if (!enemy) {
                if (ship.vertical) {
                    for (int i = y; i < y + length; i++) {
                        Cell cell = getCell(x, i);
                        cell.setFill(Color.LIGHTBLUE);  // Previsualizar la colocación
                    }
                } else {
                    for (int i = x; i < x + length; i++) {
                        Cell cell = getCell(i, y);
                        cell.setFill(Color.LIGHTBLUE);  // Previsualizar la colocación
                    }
                }
            }

            // Colocando el barco de forma definitiva
            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            } else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    if (!enemy) {
                        cell.setFill(Color.WHITE);
                        cell.setStroke(Color.GREEN);
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Obtiene una celda específica del tablero.
     *
     * @param x Coordenada X de la celda.
     * @param y Coordenada Y de la celda.
     * @return La celda correspondiente.
     */
    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    /**
     * Verifica si un barco puede colocarse en una posición específica.
     *
     * @param ship El barco a verificar.
     * @param x    Coordenada X de inicio.
     * @param y    Coordenada Y de inicio.
     * @return Verdadero si el barco puede colocarse, falso de lo contrario.
     */
    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i)) return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null) return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i)) return false;

                    if (neighbor.ship != null) return false;
                }
            }
        } else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y)) return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null) return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y)) return false;

                    if (neighbor.ship != null) return false;
                }
            }
        }
        return true;
    }

    /**
     * Obtiene los vecinos de una celda específica.
     *
     * @param x Coordenada X de la celda.
     * @param y Coordenada Y de la celda.
     * @return Array de celdas vecinas.
     */
    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[]{
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int) p.getX(), (int) p.getY()));
            }
        }
        return neighbors.toArray(new Cell[0]);
    }

    /**
     * Verifica si un punto está dentro de los límites del tablero.
     *
     * @param point Punto a verificar.
     * @return Verdadero si el punto es válido, falso de lo contrario.
     */
    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    /**
     * Verifica si las coordenadas están dentro de los límites del tablero.
     *
     * @param x Coordenada X a verificar.
     * @param y Coordenada Y a verificar.
     * @return Verdadero si las coordenadas son válidas, falso de lo contrario.
     */
    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    /**
     * Clase que representa una celda en el tablero.
     * Gestiona su estado, interacción y relación con barcos.
     */
    public class Cell extends Rectangle {
        public int x;
        public int y;
        public Ship ship = null;
        public boolean wasShot = false;
        private Board board;

        /**
         * Constructor de la clase Cell.
         *
         * @param x     Coordenada X de la celda.
         * @param y     Coordenada Y de la celda.
         * @param board Tablero al que pertenece la celda.
         */
        public Cell(int x, int y, Board board) {
            super(30, 30);
            this.x = x;
            this.y = y;
            this.board = board;
            setFill(Color.LIGHTGRAY);
            setStroke(Color.NAVY);
        }

        /**
         * Maneja el disparo a la celda.
         *
         * @return Verdadero si el disparo acertó un barco, falso de lo contrario.
         */
        public boolean shoot() {
            wasShot = true;
            setFill(Color.NAVY);

            if (ship != null) {
                ship.hit();
                ImageView hitShipImage = new ImageView(new Image(
                        getClass().getResourceAsStream("/com/example/hellojavafx/images/explosion.png")));
                hitShipImage.setFitWidth(getWidth());
                hitShipImage.setFitHeight(getHeight());
                setStroke(Color.RED);
                setFill(Color.RED);

                if (!ship.isAlive()) {
                    board.ships--;
                }

                return true;
            }
            return false;
        }
    }
}
