package com.example.controller;

import java.util.Map;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class TakeAwayController {
    @FXML
    private ImageView clearButton;

    @FXML
    private VBox takeAwayBox;

    @FXML
    TextField takeAway_NameField;

    MainController mainController;

    @FXML
    public void initialize() {
        clearButton.setOnMouseClicked(event -> {
            takeAway_NameField.setText("");
        });
        
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void loadOrderedList() {
        takeAwayBox.getChildren().clear();
        for (Map.Entry<MenuItem, Integer> entry : mainController.cart.entrySet()) {
            if (entry.getValue() > 0) {
                try {
                    FXMLLoader orderedItemLoader = new FXMLLoader(getClass().getResource("/com/example/view/orderedItem.fxml"));
                    Node orderedItemBox = orderedItemLoader.load();
                    OrderedItemController orderedItemController = orderedItemLoader.getController();
                    orderedItemController.setOrderedItem(entry.getKey(), entry.getValue());
                    orderedItemController.setTakeAwayController(this);
                    orderedItemController.setMainController(mainController);
                    takeAwayBox.getChildren().add(orderedItemBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public VBox getTakeAway() {
        return takeAwayBox;
    }

    public void clearCart() {
        takeAwayBox.getChildren().clear();
        totalPrice();
    }

    public void addToCart(MenuItem item) {
        loadOrderedList();
        totalPrice();
    }

    public void deleteOrderedItem(MenuItem item) {
        mainController.cart.remove(item);
        loadOrderedList();
        totalPrice();
    }

    public void totalPrice() {
        double total = 0.0;
        for (Map.Entry<MenuItem, Integer> entry : mainController.cart.entrySet()) {
            total += entry.getKey().getPrice()*entry.getValue();
        }
        mainController.showTotalPrice(total);
    }

}
