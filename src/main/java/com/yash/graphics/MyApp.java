package com.yash.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApp extends Application {
    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Load layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            // Config layout
            stage.setTitle("Orthographic Viewer");
            stage.setResizable(false);
            // Create scene
            final Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
            // Lunch GUI
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}