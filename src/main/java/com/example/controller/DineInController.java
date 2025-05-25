package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class DineInController {
     @FXML
    private TextField CustomerNameField;

    @FXML
    private VBox dineIn;

    @FXML
    private ScrollPane dineScroll;

    @FXML
    private TextField tableNumberField;

    @FXML
    private ImageView clearButton;

    List<MenuItem> orderedItems = new ArrayList<>();
    MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void loadOrderedList() {
        for (MenuItem menuItem : orderedItems) {
            dineIn.getChildren().clear(); // Clear previous items
            try {
                FXMLLoader orderedItemLoader = new FXMLLoader(getClass().getResource("/com/example/view/orderedItem.fxml"));
                VBox orderedItemBox = orderedItemLoader.load();
                OrderedItemController orderedItemController = orderedItemLoader.getController();
                orderedItemController.setOrderedItem(menuItem);
                orderedItemController.setDineInController(this);
                dineIn.getChildren().add(orderedItemBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VBox getDineIn() {
        return dineIn;
    }

    public void clearCart() {
        dineIn.getChildren().clear();
        CustomerNameField.clear();
        tableNumberField.clear();
        orderedItems.clear();
        totalPrice();
    }

    public void addToCart(MenuItem item) {
        orderedItems.add(item);
        loadOrderedList();
    }

    public void deleteOrderedItem(MenuItem item) {
        orderedItems.remove(item);
        loadOrderedList();
    }

    public void totalPrice() {
        double total = 0.0;
        for (MenuItem item : orderedItems) {
            total += item.getPrice();
        }
        mainController.showTotalPrice(total);
    }
}
