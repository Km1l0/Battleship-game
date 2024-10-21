package com.example.hellojavafx.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import com.example.hellojavafx.models.GameBoard;
import com.example.hellojavafx.view.alert.AlertBox; // Importa tu AlertBox
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Controlador para el juego de Sudoku.
 * Maneja la interacción del usuario y la lógica del juego.
 */
public class GameController implements Initializable {

    @FXML Button button_one;  // Botón para seleccionar el número 1
    @FXML Button button_two;  // Botón para seleccionar el número 2
    @FXML Button button_three; // Botón para seleccionar el número 3
    @FXML Button button_four; // Botón para seleccionar el número 4
    @FXML Button button_five; // Botón para seleccionar el número 5
    @FXML Button button_six;  // Botón para seleccionar el número 6
    @FXML Canvas canvas;      // Canvas donde se dibuja el tablero
    @FXML TextArea textAreaa; // Área de texto para mostrar números y mensajes

    int player_selected_row;   // Fila seleccionada por el jugador
    int player_selected_col;   // Columna seleccionada por el jugador

    // Instancia de GameBoard
    GameBoard gameboard;

    // Instancia de AlertBox
    AlertBox alertBox;

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Crea una instancia de GameBoard y configura el canvas.
     *
     * @param arg0 URL de la ubicación del FXML.
     * @param arg1 Recursos de la interfaz de usuario.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gameboard = new GameBoard(); // Crear una instancia de nuestro tablero de juego
        alertBox = new AlertBox();    // Crear una instancia de AlertBox
        GraphicsContext context = canvas.getGraphicsContext2D();
        drawOnCanvas(context);

        // Listener para el TextArea
        textAreaa.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("[1-6]?")) {
                if (!newValue.isEmpty() && player_selected_row >= 0 && player_selected_col >= 0) {
                    int val = Integer.parseInt(newValue);
                    if (gameboard.isValidMove(val, player_selected_row, player_selected_col)) {
                        gameboard.modifyPlayer(val, player_selected_row, player_selected_col);
                        drawOnCanvas(canvas.getGraphicsContext2D());
                    } else {
                        showInvalidMoveAlert(); // Usar AlertBox aquí
                    }
                }
            } else {
                textAreaa.setText(oldValue);
            }
        });
    }

    /**
     * Método que se ejecuta cuando se presiona el botón 1.
     */
    public void buttonOnePressed() { handleButtonPressed(1); }

    /**
     * Método que se ejecuta cuando se presiona el botón 2.
     */
    public void buttonTwoPressed() { handleButtonPressed(2); }

    /**
     * Método que se ejecuta cuando se presiona el botón 3.
     */
    public void buttonThreePressed() { handleButtonPressed(3); }

    /**
     * Método que se ejecuta cuando se presiona el botón 4.
     */
    public void buttonFourPressed() { handleButtonPressed(4); }

    /**
     * Método que se ejecuta cuando se presiona el botón 5.
     */
    public void buttonFivePressed() { handleButtonPressed(5); }

    /**
     * Método que se ejecuta cuando se presiona el botón 6.
     */
    public void buttonSixPressed() { handleButtonPressed(6); }

    /**
     * Maneja la lógica común para presionar cualquier botón del número.
     *
     * @param value El valor correspondiente al botón presionado.
     */
    private void handleButtonPressed(int value) {
        if (gameboard.isValidMove(value, player_selected_row, player_selected_col)) {
            gameboard.modifyPlayer(value, player_selected_row, player_selected_col);
            drawOnCanvas(canvas.getGraphicsContext2D());
        } else {
            showInvalidMoveAlert(); // Usar AlertBox aquí
        }
    }

    /**
     * Muestra una alerta cuando se realiza un movimiento no válido.
     */
    private void showInvalidMoveAlert() {
        alertBox.showAlert("Movimiento no válido", null, "Este movimiento no es válido. Intenta de nuevo."); // Usar AlertBox aquí
    }

    /**
     * Dibuja el tablero y los números en el canvas.
     *
     * @param context El contexto gráfico del canvas.
     */
    public void drawOnCanvas(GraphicsContext context) {
        context.clearRect(0, 0, 320, 320);
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int position_y = row * 50 + 2;
                int position_x = col * 50 + 2;
                int width = 46;
                context.setFill(Color.WHITE);
                context.fillRoundRect(position_x, position_y, width, width, 10, 10);
            }
        }

        context.setStroke(Color.RED);
        context.setLineWidth(5);
        context.strokeRoundRect(player_selected_col * 50 + 2, player_selected_row * 50 + 2, 46, 46, 10, 10);

        int[][] initial = gameboard.getInitial();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int position_y = row * 50 + 30;
                int position_x = col * 50 + 20;
                context.setFill(Color.BLACK);
                context.setFont(new Font(20));
                if (initial[row][col] != 0) {
                    context.fillText(initial[row][col] + "", position_x, position_y);
                }
            }
        }

        int[][] player = gameboard.getPlayer();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int position_y = row * 50 + 30;
                int position_x = col * 50 + 20;
                context.setFill(Color.PURPLE);
                context.setFont(new Font(20));
                if (player[row][col] != 0) {
                    context.fillText(player[row][col] + "", position_x, position_y);
                }
            }
        }

        if (gameboard.checkForSuccessGeneral()) {
            context.clearRect(0, 0, 450, 450);
            context.setFill(Color.GREEN);
            context.setFont(new Font(36));
            context.fillText("SUCCESS!", 150, 250);
        }
    }

    /**
     * Configura el evento de clic del mouse en el canvas para seleccionar una celda.
     */
    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int mouse_x = (int) event.getX();
                int mouse_y = (int) event.getY();
                player_selected_row = (int) (mouse_y / 50);
                player_selected_col = (int) (mouse_x / 50);
                drawOnCanvas(canvas.getGraphicsContext2D());
            }
        });
    }

    /**
     * Maneja el clic del botón "Ayuda" para mostrar un número válido en la celda seleccionada.
     *
     * @param mouseEvent El evento del clic del mouse.
     */
    public void getHelpButtonClicked(MouseEvent mouseEvent) {
        if (player_selected_row >= 0 && player_selected_col >= 0) {
            int helpNumber = gameboard.getValidNumberAtPosition(player_selected_row, player_selected_col);
            if (helpNumber != -1) {
                textAreaa.setText(String.valueOf(helpNumber));
            } else {
                alertBox.showAlert("Sin ayuda", null, "No hay números válidos disponibles para esta celda."); // Usar AlertBox aquí
            }
        } else {
            alertBox.showAlert("Sin celda seleccionada", null, "No hay celda seleccionada."); // Usar AlertBox aquí
        }
    }
}