package com.example.controller;

import java.util.List;

import com.example.model.MenuItem;
import com.example.dao.MenuItemDAO;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class MainController {
    @FXML
    Circle logoCircle;
    @FXML
    BorderPane MainUI;
    @FXML 
    FlowPane foodItems;
    @FXML
    VBox orderList;
    @FXML
    Button dineInButton;
    @FXML
    Button takeAwayButton;  
    @FXML
    Region hightLightRegion;
    @FXML
    Label subtotalPrice;
    @FXML
    Label taxPrice;
    @FXML
    Label totalPrice;
    @FXML
    Button clearButton;
    @FXML
    Button payButton;

    MenuItemDAO menuItemDAO = new MenuItemDAO();
    MenuCardController menuCardController;
    List<MenuItem> menuItems = menuItemDAO.findAll();
    DineInController dineInController;
    TakeAwayController takeAwayController;
    String orderType = "DineIn";
    Button curreButton = dineInButton; //for highlighting the current button
    @FXML
    public void initialize() {
        setLogo();
        loadMenuItemsToMenu();
        switchToDineInUI();
        butonSetOnAction();
    }

    private void loadMenuItemsToMenu(){
        foodItems.getChildren().clear();
        for (MenuItem item : menuItems) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/view/menuCard.fxml"));
                Node card = cardLoader.load();
                MenuCardController cardController = cardLoader.getController();
                cardController.setMenuItem(item);
                cardController.setMainController(this);
                foodItems.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setLogo() {
        try {
            Image logoImage = new Image(getClass().getResourceAsStream("/com/example/image/logo.jpg"));
            logoCircle.setFill(new javafx.scene.paint.ImagePattern(logoImage));
        } catch (Exception e) {
            System.err.println("Logo image not found: " + e.getMessage());
        }
    }

    private void switchToDineInUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/DineIn.fxml"));
            VBox dineInUI = loader.load();
            dineInController = loader.getController();
            dineInController.setMainController(this);
            orderList.getChildren().clear();
            orderList.getChildren().add(dineInUI);
            orderType = "DineIn";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToTakeAwayUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/TakeAway.fxml"));
            VBox takeAwayUI = loader.load();
            takeAwayController = loader.getController();
            takeAwayController.setMainController(this);
            orderList.getChildren().clear();
            orderList.getChildren().add(takeAwayUI);
            orderType = "TakeAway";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTotalPrice(double price) {
        double tax = price * 0.08;
        double total = price + tax;
        // Format the prices to display with ".000 VNĐ"
        subtotalPrice.setText(String.format("%.3f VNĐ", price/1000));
        taxPrice.setText(String.format("%.3f VNĐ", tax/1000));
        totalPrice.setText(String.format("%.3f VNĐ", total/1000));
        payButton.setText(String.format("Pay %.3f VNĐ", total/1000));
    }

    private void butonSetOnAction() {
        dineInButton.setOnAction(event -> switchToDineInUI());
        takeAwayButton.setOnAction(event -> switchToTakeAwayUI());
        clearButton.setOnAction(event -> {
            if (orderType.equals("DineIn")) {
                dineInController.clearCart();;
                dineInController.orderedItems.clear();
                showTotalPrice(0);
            } else {
                takeAwayController.clearCart();
                takeAwayController.orderedItems.clear();
                showTotalPrice(0);
            }
        });
    }

}
