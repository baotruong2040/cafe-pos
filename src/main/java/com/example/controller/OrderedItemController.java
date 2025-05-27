package com.example.controller;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
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
    MenuItem menuItem;

    public void setDineInController(DineInController dineInController) {
        this.dineInController = dineInController;
    }

    public void setTakeAwayController(TakeAwayController takeAwayController) {
        this.takeAwayController = takeAwayController;
    }

    public void setOrderedItem(MenuItem item) {
        this.menuItem = item;
        orderedItemName.setText(item.getName());
        orderedItemPrice.setText(String.format("%,.0f VNĐ", item.getPrice()));
        String imagePath = item.getImagePath();
        try {
            Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath));
            orderedImage.setFill(new javafx.scene.paint.ImagePattern(image));
        } catch (Exception e) {
            Image image = new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/image/placeHolder.png"));
            orderedImage.setFill(new javafx.scene.paint.ImagePattern(image));
        }
        deleteButton.setOnAction(event -> {
            if (dineInController != null) {
                dineInController.deleteOrderedItem(menuItem);
                dineInController.mainController.loadMenuItemsToMenu(dineInController.mainController.currentCategory);
            } else if (takeAwayController != null) {
                takeAwayController.deleteOrderedItem(menuItem);
                takeAwayController.mainController.loadMenuItemsToMenu(takeAwayController.mainController.currentCategory);
            }
        });
    }

    
}
