package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.model.MenuItem;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DineInController {
    @FXML
    private TextField dineIn_NameField;

    @FXML
    private VBox dineIn;

    @FXML
    private ScrollPane dineScroll;

    @FXML
    private TextField dineIN_NumberField;

    @FXML
    private ImageView clearButton;

    List<MenuItem> orderedItems = new ArrayList<>();
    MainController mainController;

    @FXML
    public void initialize() {
        clearButton.setOnMouseClicked(event -> {
            dineIn_NameField.setText("");
            dineIN_NumberField.setText("");
        });
        dineScroll.setFitToWidth(true);
        dineScroll.setFitToHeight(true);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void loadOrderedList() {
        dineIn.getChildren().clear();
        Platform.runLater(() -> {
            for (MenuItem menuItem : orderedItems) {
            try {
                FXMLLoader orderedItemLoader = new FXMLLoader(getClass().getResource("/com/example/view/orderedItem.fxml"));
                Node orderedItemBox = orderedItemLoader.load();
                OrderedItemController orderedItemController = orderedItemLoader.getController();
                orderedItemController.setOrderedItem(menuItem);
                orderedItemController.setDineInController(this);
                dineIn.getChildren().add(orderedItemBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        });
    }

    public VBox getDineIn() {
        return dineIn;
    }

    public void clearCart() {
        dineIn.getChildren().clear();
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
