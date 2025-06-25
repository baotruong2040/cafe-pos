package com.example.controller;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

public class MenuCardController {
    @FXML
    private Button addButton;

    @FXML
    private Rectangle cardImage;

    @FXML
    private HBox menuCard;

    @FXML
    private Button minusButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label quantityLabel;

    MainController mainController;
    MenuItem menuItem;
    int quantity = 0;
    @FXML
    public void initialize() {
        addButton.setOnAction(event -> {
            int newQuantity = mainController.cart.getOrDefault(menuItem, 0) + 1;
            mainController.cart.put(menuItem, newQuantity);
            mainController.updateAllMenuCardQuantity(menuItem, newQuantity);
            if (mainController.orderType.equals("DineIn")) {
                mainController.dineInController.addToCart(menuItem);
            } else {
                mainController.takeAwayController.addToCart(menuItem);
            }
        });
        minusButton.setOnAction(event -> {
            int newQuantity = mainController.cart.getOrDefault(menuItem, 0) - 1;
            if (newQuantity < 0) newQuantity = 0;
            mainController.cart.put(menuItem, newQuantity);
            mainController.updateAllMenuCardQuantity(menuItem, newQuantity);
            if (mainController.orderType.equals("DineIn")) {
                mainController.dineInController.addToCart(menuItem);
            } else {
                mainController.takeAwayController.addToCart(menuItem);
            }
        });
        minusButton.setDisable(true);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setMenuItem(MenuItem item) {
        this.menuItem = item;
        nameLabel.setText(item.getName());
        priceLabel.setText(String.format("%,.0f VNĐ", item.getPrice()));
        int q = mainController.cart.getOrDefault(item, 0);
        setQuantity(q);
        
        loadMenuItemImage(item.getImagePath());
    }

    private void loadMenuItemImage(String imagePath) {
        
        try {
            Image placeholder = new Image(getClass().getResourceAsStream("/com/example/image/placeHolder.png"));
            cardImage.setFill(new javafx.scene.paint.ImagePattern(placeholder));
        } catch (Exception e) {
            System.out.println("Image Error");
        }
        new Thread(() -> {
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                
                javafx.application.Platform.runLater(() -> {
                    if (!image.isError()) {
                        cardImage.setFill(new javafx.scene.paint.ImagePattern(image));
                    }
                });
            } catch (Exception e) {
                System.out.println("Image Error");
            }
        }).start();
    }

    public void clearQuantity() {
        quantity = 0;
        quantityLabel.setText(String.valueOf(quantity));
        minusButton.setDisable(true);
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
        quantityLabel.setText(String.valueOf(quantity));
        minusButton.setDisable(quantity < 1);
    }
}
