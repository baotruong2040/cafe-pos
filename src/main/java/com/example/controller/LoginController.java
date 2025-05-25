package com.example.controller;

import com.example.dao.UserDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    TextField usernameField;
    @FXML
    TextField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Rectangle loginLogo;

    UserDAO userDAO = new UserDAO();
    MainController mainController;

    @FXML
    public void initialize() {
        setLogo();
        loginButton.setOnAction(event -> handleLogin());
    }
    
    private void setLogo() {
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/com/example/image/Java.png"));
            loginLogo.setFill(new javafx.scene.paint.ImagePattern(logoImage));
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!username.isEmpty() || !password.isEmpty()) {
            if (userDAO.isValidUser(username, password)) {
                if (userDAO.getUserRole(username).equalsIgnoreCase("admin")) {
                    switchToMainUI();
                } else {
                    System.out.println("You do not have admin privileges");
                    
                }
            } else {
                System.out.println("Invalid username or password");
                
            }
            
        }else {
            System.out.println("Please enter both username and password");
        }
    }

    private void switchToMainUI() {
        try {
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/MainUI.fxml"));
            BorderPane mainUI = loader.load();
            mainController = loader.getController();
            Scene scene = new Scene(mainUI, 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/com/example/view/style.css").toExternalForm());
            primaryStage.setTitle("Café Management System");
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setMinWidth(610);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
