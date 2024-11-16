package com.example.hellojavafx.controllers;

import com.example.hellojavafx.models.Ship;
import com.example.hellojavafx.view.alert.AlertBox;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import java.util.Random;

/**
 * Controlador principal del juego de Batalla Naval.
 * Maneja la lógica del juego, incluyendo la inicialización de los tableros,
 * el manejo de turnos, y la detección de condiciones de victoria o derrota.
 */
public class GameController {

    /** Indica si el juego está en progreso. */
    private boolean running = false;

    /** Tablero del enemigo. */
    private Board enemyBoard;

    /** Tablero del jugador. */
    private Board playerBoard;

    /** Número de barcos restantes por colocar para el jugador. */
    private int shipsToPlace = 4;

    /** Indica si es el turno del enemigo. */
    private boolean enemyTurn = false;

    /** Generador de números aleatorios para los movimientos del enemigo. */
    private Random random = new Random();

    /** Contenedor del tablero del jugador. */
    @FXML
    private AnchorPane pane1;

    /** Contenedor del tablero del enemigo. */
    @FXML
    private AnchorPane pane2;

    /** Imagen representativa del barco 1. */
    @FXML
    private ImageView b1;

    /** Imagen representativa del barco 2. */
    @FXML
    private ImageView b2;

    /** Imagen representativa del barco 3. */
    @FXML
    private ImageView b3;

    /** Imagen representativa del barco 4. */
    @FXML
    private ImageView b4;

    /**
     * Método de inicialización que configura los tableros del jugador y del enemigo.
     * También define los manejadores de eventos para cada tablero.
     */
    public void initialize() {
        // Inicialización del tablero del enemigo
        enemyBoard = new Board(true, event -> {
            if (!running) return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (cell.wasShot) return;

            enemyTurn = !cell.shoot();

            if (enemyBoard.ships == 0) {
                System.out.println("YOU WIN");

                AlertBox alertBox = new AlertBox();
                alertBox.showAlert("Victoria", "Felicidades!", "Destruiste todos los barcos enemigos");
            }

            if (enemyTurn) {
                enemyMove();
            }
        });

        // Inicialización del tablero del jugador
        playerBoard = new Board(false, event -> {
            if (running) return;

            Board.Cell cell = (Board.Cell) event.getSource();
            if (playerBoard.placeShip(new Ship(shipsToPlace, event.getButton() == MouseButton.PRIMARY), cell.x, cell.y)) {
                if (--shipsToPlace == 0) {
                    startGame();
                }
            }
        });

        // Añadir los tableros a los contenedores correspondientes
        pane1.getChildren().add(playerBoard);  // Tablero del jugador
        pane2.getChildren().add(enemyBoard);  // Tablero del enemigo
    }

    /**
     * Maneja el turno del enemigo, permitiéndole realizar disparos aleatorios en el tablero del jugador.
     * Si todos los barcos del jugador son destruidos, muestra una alerta indicando la derrota.
     */
    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            Board.Cell cell = playerBoard.getCell(x, y);
            if (cell.wasShot)
                continue;

            enemyTurn = cell.shoot();

            if (playerBoard.ships == 0) {
                System.out.println("YOU LOSE");
                AlertBox alertBox = new AlertBox();
                alertBox.showAlert("Derrota", "Lo siento!", "Todos tus barcos fueron eliminados");
            }
        }
    }

    /**
     * Inicia el juego una vez que todos los barcos del jugador han sido colocados.
     * También posiciona los barcos del enemigo aleatoriamente en su tablero.
     */
    private void startGame() {
        int type = 4;
        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);

            if (enemyBoard.placeShip(new Ship(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }
        running = true;
    }
}
