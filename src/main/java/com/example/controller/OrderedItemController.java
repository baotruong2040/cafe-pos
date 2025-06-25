package com.example.controller;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class OrderedItemController {
    @FXML
    private Button deleteButton;

    @FXML
    private Rectangle orderedImage;

    @FXML
    private HBox orderedItemCard;

    @FXML
    private Label orderedItemName;

    @FXML
    private Label orderedItemPrice;

    DineInController dineInController;
    TakeAwayController takeAwayController;
    MainController mainController;
    MenuItem menuItem;

    public void setDineInController(DineInController dineInController) {
        this.dineInController = dineInController;
    }

    public void setTakeAwayController(TakeAwayController takeAwayController) {
        this.takeAwayController = takeAwayController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setOrderedItem(MenuItem item, Integer quantity) {
        this.menuItem = item;
        orderedItemName.setText(item.getName());
        orderedItemPrice.setText(String.format("%,.0f VNĐ * "+quantity, item.getPrice()));
        String imagePath = item.getImagePath();
        try {
            Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath));
            orderedImage.setFill(new ImagePattern(image));
        } catch (Exception e) {
            Image image = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/image/placeHolder.png"));
            orderedImage.setFill(new ImagePattern(image));
        }
        deleteButton.setOnAction(event -> {
            mainController.cart.remove(menuItem);
            mainController.updateAllMenuCardQuantity(menuItem, 0); 
            if (dineInController != null) {
                dineInController.loadOrderedList();
                dineInController.totalPrice();
            } else if (takeAwayController != null) {
                takeAwayController.loadOrderedList();
                takeAwayController.totalPrice();
            }
        });
    }

    
}
