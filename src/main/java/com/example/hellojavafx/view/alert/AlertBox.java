package com.example.hellojavafx.view.alert;

import javafx.scene.control.Alert;

public class AlertBox implements AlertBoxInterface {
    @Override
    public void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
