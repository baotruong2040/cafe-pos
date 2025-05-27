package com.example.controller;

import com.example.dao.MenuItemDAO;
import com.example.model.MenuItem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductCardController {
    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private VBox productCard;

    @FXML
    private ImageView productCardImage;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLabel;

    @FXML
    private Label statusLabel;

    MenuItem menuItem;
    ProductController productController;

    @FXML
    public void initialize() {
        deleteButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Are you sure you want to delete this product?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    deleteProduct();
                }
            });
        });

        editButton.setOnAction(event -> {
            updateProduct();
        });
    }

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    public void setProduct(MenuItem item) {
        this.menuItem = item;

        productNameLabel.setText(item.getName());
        productPriceLabel.setText(String.format("%,.0f VNĐ", item.getPrice()));
        statusLabel.setText(item.isAvailable() ? "Available" : "Unvailable");
        statusLabel.setStyle(item.isAvailable() ? "-fx-background-color:  #10b981; -fx-background-radius: 20;" : "-fx-background-color: red; -fx-background-radius: 20;");
        
        try {
            Image image = new Image(getClass().getResourceAsStream(item.getImagePath()));
            productCardImage.setImage(image);
        } catch (Exception e) {
            Image image = new Image(getClass().getResourceAsStream("/com/example/image/placeHolder.png"));
            productCardImage.setImage(image);
        }
    }
    public void deleteProduct() {
        MenuItemDAO menuItemDAO = new MenuItemDAO();
        if (menuItem != null) {
            menuItemDAO.delete(menuItem);
            productController.loadProducts("", productController.selectedCategory, productController.selectedStatus);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Product");
            alert.setHeaderText("Product Deleted Successfully");
            alert.setContentText("The product has been deleted from the menu.");
            alert.showAndWait();
        } else {
            System.out.println("No product selected for deletion.");
        }
    }

    public void updateProduct() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/AddProductForm.fxml"));
            Parent addProductForm = loader.load();
            AddProductFormController addProductFormController = loader.getController();
            addProductFormController.setProductController(productController);
            addProductFormController.setProductItem(menuItem);
            Stage stage = new Stage();
            stage.setTitle("Edit Product");
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/image/Java.png")));
            stage.setScene(new Scene(addProductForm, 400, 620));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load product edit form");
            alert.setContentText("An error occurred while trying to load the product edit form.");
            alert.showAndWait();
        }
    }
}
