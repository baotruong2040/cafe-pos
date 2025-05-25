package com.example.controller;

import java.util.ArrayList;
import java.util.List;

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
    private TextField takeAway_NameField;

    List<MenuItem> orderedItems = new ArrayList<>();
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
        for (MenuItem menuItem : orderedItems) {
            try {
                FXMLLoader orderedItemLoader = new FXMLLoader(getClass().getResource("/com/example/view/orderedItem.fxml"));
                Node orderedItemBox = orderedItemLoader.load();
                OrderedItemController orderedItemController = orderedItemLoader.getController();
                orderedItemController.setOrderedItem(menuItem);
                orderedItemController.setTakeAwayController(this);
                takeAwayBox.getChildren().add(orderedItemBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VBox getTakeAway() {
        return takeAwayBox;
    }

    public void clearCart() {
        takeAwayBox.getChildren().clear();
        orderedItems.clear();
        totalPrice();
    }

    public void addToCart(MenuItem item) {
        orderedItems.add(item);
        loadOrderedList();
        totalPrice();
    }

    public void deleteOrderedItem(MenuItem item) {
        orderedItems.remove(item);
        loadOrderedList();
        totalPrice();
    }

    public void totalPrice() {
        double total = 0.0;
        for (MenuItem item : orderedItems) {
            total += item.getPrice();
        }
        mainController.showTotalPrice(total);
    }

}
