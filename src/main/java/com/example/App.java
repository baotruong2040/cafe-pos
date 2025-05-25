package com.example;

import com.example.util.HibernateUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate initialized successfully.");
        } catch (Exception e) {
            System.err.println("Failed to initialize Hibernate: " + e.getMessage());
            e.printStackTrace();
            return; // Exit if Hibernate fails to initialize
        }
        
        FXMLLoader rootLoader = new FXMLLoader(getClass().getResource("/com/example/view/loginUI.fxml"));
        HBox root = rootLoader.load();
        Scene scene = new javafx.scene.Scene(root, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/com/example/view/style.css").toExternalForm());
        primaryStage.setTitle("Login - Café Management System");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/image/Java.png")));
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setMinWidth(610);
        primaryStage.show();
    }

}
