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

    MainController mainController;
    MenuItem menuItem;

    @FXML
    public void initialize() {
        addButton.setOnAction(event -> addToCart());

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
        priceLabel.setText(String.valueOf(item.getPrice())+" VNĐ");
        String imagePath = item.getImagePath();
        Image image = null;
        try {
            image = new Image(getClass().getResourceAsStream(imagePath));
            cardImage.setFill(new javafx.scene.paint.ImagePattern(image));
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }
    }

    public void addToCart() {
        if (mainController.orderType.equals("DineIn")) {
            mainController.dineInController.addToCart(menuItem);
        } else if (mainController.orderType.equals("TakeAway")) {
            mainController.takeAwayController.addToCart(menuItem);
        }
    }
    public void removeFromCart() {
        if (mainController.orderType.equals("DineIn")) {
            mainController.dineInController.deleteOrderedItem(menuItem);;
        } else if (mainController.orderType.equals("TakeAway")) {
            mainController.takeAwayController.deleteOrderedItem(menuItem);;
        }
    }
}
