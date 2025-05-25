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

    public void setDineInController(DineInController dineInController) {
        this.dineInController = dineInController;
    }

    public void setTakeAwayController(TakeAwayController takeAwayController) {
        this.takeAwayController = takeAwayController;
    }

    public void setOrderedItem(MenuItem item) {
        orderedItemName.setText(item.getName());
        orderedItemPrice.setText(String.valueOf(item.getPrice())+" VNĐ");
        String imagePath = item.getImagePath();
        try {
            Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(imagePath));
            orderedImage.setFill(new javafx.scene.paint.ImagePattern(image));
        } catch (Exception e) {
            System.out.println("Image not found: " + imagePath);
        }
    }

    
}
