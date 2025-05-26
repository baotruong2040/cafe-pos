package com.example.controller;

import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ProductCardController {
    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private VBox productCard;

    @FXML
    private Rectangle productCardImage;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label statusLabel;

    MenuItem menuItem;
    ProductController productController;

    public void setMainController(ProductController productController) {
        this.productController = productController;
    }

    public void setProduct(MenuItem item) {
        this.menuItem = item;

        productNameLabel.setText(item.getName());
        productPriceLabel.setText(String.format(item.getPrice()+" VNĐ"));
        statusLabel.setText(item.isAvailable() ? "Còn hàng" : "Hết hàng");
        
        try {
            Image image = new Image(getClass().getResourceAsStream(item.getImagePath()));
            productCardImage.setFill(new javafx.scene.paint.ImagePattern(image));
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        

        
    }
}
