package com.example.hellojavafx.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Stage {

    public WelcomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/hellojavafx/welcome-view.fxml")
        );
        Parent root = loader.load();
        this.setTitle("Craps Game");
        Scene scene = new Scene(root);
        this.getIcons().add(new Image(
                getClass().getResourceAsStream("/com/example/hellojavafx/images/favicon.png")
        ));
        this.setScene(scene);
        this.show();
    }

    public static WelcomeView getInstance() throws IOException {
        return WelcomeViewHolder.INSTANCE = new WelcomeView();
    }

    private static class WelcomeViewHolder {
        private static WelcomeView INSTANCE;
    }
}






















