package com.example.hellojavafx.controllers;

import com.example.hellojavafx.view.GameView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;

public class WelcomeController {

    @FXML
    void startButtn(ActionEvent event) {
        try {
            // Instancia de la ventana del juego y muestra GameView
            GameView gameView = GameView.getInstance();
            gameView.show(); // Mostrar la vista del juego
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de excepciones si no se puede cargar el FXML de la vista del juego
        }
    }

    public void contButtn(ActionEvent actionEvent) {
        // Cerrar la aplicación inmediatamente
        Platform.exit();  // Esto cerrará la aplicación de manera segura
    }
}
