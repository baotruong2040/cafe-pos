package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    TextField dineIn_NameField;

    @FXML
    private VBox dineIn;

    @FXML
    private ScrollPane dineScroll;

    @FXML
    TextField dineIN_NumberField;

    @FXML
    private ImageView clearButton;

    MainController mainController;

    List<OrderedItemController> orderedItemControllers = new ArrayList<>();

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
            for (Map.Entry<MenuItem, Integer> entry : mainController.cart.entrySet()) {
                if (entry.getValue() > 0) {
                    try {
                    FXMLLoader orderedItemLoader = new FXMLLoader(getClass().getResource("/com/example/view/orderedItem.fxml"));
                    Node orderedItemBox = orderedItemLoader.load();
                    OrderedItemController orderedItemController = orderedItemLoader.getController();
                    orderedItemController.setOrderedItem(entry.getKey(), entry.getValue());
                    orderedItemController.setDineInController(this);
                    orderedItemController.setMainController(mainController);
                    orderedItemControllers.add(orderedItemController);
                    dineIn.getChildren().add(orderedItemBox);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
    }

    public VBox getDineIn() {
        return dineIn;
    }

    public void clearCart() {
        dineIn.getChildren().clear();
        orderedItemControllers.clear();
        totalPrice();
    }

    public void addToCart(MenuItem item) {
        loadOrderedList();
        totalPrice();
    }

    public void deleteOrderedItem(MenuItem item) {
        loadOrderedList();
        totalPrice();
    }

    public void totalPrice() {
        double total = 0.0;
        for (Map.Entry<MenuItem, Integer> entry : mainController.cart.entrySet()) {
            total +=entry.getKey().getPrice()*entry.getValue();
        }
        mainController.showTotalPrice(total);
    }
}
