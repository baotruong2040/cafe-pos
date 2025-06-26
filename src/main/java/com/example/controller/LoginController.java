package com.example.controller;

import com.example.dao.UserDAO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button loginButton;
    @FXML
    Rectangle loginLogo;
    @FXML
    Label notFillWarn;
    @FXML
    Label inValidSignIn;
    UserDAO userDAO = new UserDAO();
    MainController mainController;

    @FXML
    public void initialize() {
        setLogo();
        loginButton.setOnAction(event -> handleLogin());
        passwordField.setOnAction(event -> handleLogin());
        usernameField.setOnAction(event -> handleLogin());
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
        new Thread(() -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (!username.isEmpty() && !password.isEmpty()) {
                if (userDAO.isValidUser(username, password)) {
                    inValidSignIn.setVisible(false);
                    if (userDAO.getUserRole(username).equalsIgnoreCase("admin")) {
                        Platform.runLater(() -> switchToMainUI(true));
                    } else {
                        Platform.runLater(() -> {
                            switchToMainUI(false);
                            mainController.dashboardNavButton.setDisable(true);
                            mainController.productNavButton.setDisable(true);
                            mainController.settingNavButton.setDisable(true);
                        });
                    }
                } else {
                    inValidSignIn.setVisible(true);
                    
                }
                notFillWarn.setVisible(false);
            }else {
                notFillWarn.setVisible(true);
            }
        }).start();
    }

    private void switchToMainUI(boolean b) {
        new Thread(() -> {
            try {
                Stage primaryStage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/MainUI.fxml"));
                BorderPane mainUI = loader.load();
                Platform.runLater(() -> {
                    mainController = loader.getController();
                    Scene scene = new Scene(mainUI, 1280, 800);
                    scene.getStylesheets().add(getClass().getResource("/com/example/view/style.css").toExternalForm());
                    primaryStage.setTitle("Café Management System");
                    primaryStage.setScene(scene);
                    primaryStage.setMinWidth(610);
                    primaryStage.show();
                    if (!b) {
                        mainController.dashboardNavButton.setDisable(true);
                        mainController.productNavButton.setDisable(true);
                        mainController.settingNavButton.setDisable(true);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
