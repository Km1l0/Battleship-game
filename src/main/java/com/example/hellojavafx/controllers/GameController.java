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

    /** Cantidad de barcos restantes por colocar para el jugador. */
    private int remainingAircraftCarriers = 1;  // 1 Portaaviones (4 casillas)
    private int remainingSubmarines = 2;        // 2 Submarinos (3 casillas)
    private int remainingDestroyers = 3;        // 3 Destructores (2 casillas)
    private int remainingFrigates = 4;          // 4 Fragatas (1 casilla)

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
            boolean vertical = event.getButton() == MouseButton.PRIMARY;

            // Determinar el tamaño del próximo barco a colocar
            int shipSize = getNextShipSize();  // Obtener el tamaño del barco según lo que queda por colocar

            if (shipSize == 0) {
                // Si no hay más barcos por colocar, no hacer nada
                return;
            }

            // Crear el barco con el tamaño y la orientación adecuados
            Ship ship = new Ship(shipSize, vertical);

            // Intentar colocar el barco en la celda seleccionada
            if (playerBoard.placeShip(ship, cell.x, cell.y)) {
                // Reducir la cantidad de barcos restantes del tipo correspondiente
                updateRemainingShips(shipSize);

                // Si ya no quedan barcos por colocar, iniciar el juego
                if (allShipsPlaced()) {
                    startGame();
                }
            }
        });

        // Añadir los tableros a los contenedores correspondientes
        pane1.getChildren().add(playerBoard);  // Tablero del jugador
        pane2.getChildren().add(enemyBoard);  // Tablero del enemigo
    }

    /**
     * Obtiene el tamaño del siguiente barco a colocar.
     * Devuelve el tamaño del barco según el tipo de barco que queda por colocar.
     */
    private int getNextShipSize() {
        if (remainingAircraftCarriers > 0) {
            return 4;  // Portaaviones
        } else if (remainingSubmarines > 0) {
            return 3;  // Submarino
        } else if (remainingDestroyers > 0) {
            return 2;  // Destructor
        } else if (remainingFrigates > 0) {
            return 1;  // Fragata
        }
        return 0;  // No quedan barcos
    }

    /**
     * Actualiza la cantidad de barcos restantes después de colocar un barco.
     */
    private void updateRemainingShips(int shipSize) {
        if (shipSize == 4) {
            remainingAircraftCarriers--;
        } else if (shipSize == 3) {
            remainingSubmarines--;
        } else if (shipSize == 2) {
            remainingDestroyers--;
        } else if (shipSize == 1) {
            remainingFrigates--;
        }
    }

    /**
     * Verifica si todos los barcos han sido colocados.
     * @return Verdadero si todos los barcos han sido colocados, falso si no.
     */
    private boolean allShipsPlaced() {
        return remainingAircraftCarriers == 0 && remainingSubmarines == 0 && remainingDestroyers == 0 && remainingFrigates == 0;
    }

    /**
     * Inicia el juego una vez que todos los barcos del jugador han sido colocados.
     * También posiciona los barcos del enemigo aleatoriamente en su tablero.
     */
    private void startGame() {
        // Colocación aleatoria de los barcos del enemigo
        placeEnemyShips();

        running = true;
    }

    /**
     * Coloca aleatoriamente los barcos del enemigo en su tablero.
     */
    private void placeEnemyShips() {
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1}; // Un portaaviones, 2 submarinos, 3 destructores y 4 fragatas
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(10);
                int y = random.nextInt(10);
                boolean vertical = random.nextBoolean();
                Ship ship = new Ship(size, vertical);
                placed = enemyBoard.placeShip(ship, x, y);
            }
        }
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
}
