package com.example.controller;

import java.util.List;

import com.example.dao.MenuItemDAO;
import com.example.model.MenuItem;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ProductController {
    @FXML
    ComboBox<String> StatusBox;

    @FXML
    Button addProductButton;

    @FXML
    ComboBox<String> categoryBox;

    @FXML
    FlowPane productList;

    @FXML
    TextField productSearchField;

    @FXML
    Button refreshButton;

    MainController mainController;
    MenuItemDAO menuItemDAO = new MenuItemDAO();
    String selectedCategory = "All";
    String selectedStatus = "All";
    String searchText = "";
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    @FXML
    public void initialize() {
        loadComboBox();
        loadProducts("", selectedCategory, selectedStatus);
        System.out.println("ProductController initialized.");
        productSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue.trim();
            loadProducts(searchText, selectedCategory, selectedStatus);
        });
        refreshButton.setOnAction(event -> {
            productSearchField.clear();
            selectedCategory = "All";
            selectedStatus = "All";
            categoryBox.setValue("All");
            StatusBox.setValue("All");
            loadProducts("", selectedCategory, selectedStatus);
        });
        addProductButton.setOnAction(event -> {
            showAddProductFormDialog();
            addProductButton.setDisable(true);
        });
    }

    private void loadComboBox() {
        ObservableList<String> categories = FXCollections.observableArrayList(
            "All",
            "Cà Phê",
            "Trà",
            "Bánh Ngọt",
            "Nước Ép"
        );
        categoryBox.setItems(categories);
        categoryBox.setValue("All");
        categoryBox.setOnAction(event -> {
            selectedCategory = categoryBox.getValue();
            loadProducts("",selectedCategory, selectedStatus);
        });

        ObservableList<String> available = FXCollections.observableArrayList(
            "All",
            "Available",
            "Unavailable"
        );
        StatusBox.setItems(available);
        StatusBox.setValue("All");
        StatusBox.setOnAction(event -> {
            selectedStatus = StatusBox.getValue();
            loadProducts("", selectedCategory, selectedStatus);
        });
    }

    public void loadProducts(String name, String category, String status) {
        
        List<MenuItem> items;
        if (name.equals("") && category.equalsIgnoreCase("All") && status.equalsIgnoreCase("All")) {
            items = menuItemDAO.findAll();
        }else {
            boolean isAvailable;
            if (status.equalsIgnoreCase("Available")) {
                isAvailable = true;
            } else if (status.equalsIgnoreCase("Unavailable")) {
                isAvailable = false;
            } else {
                isAvailable = true;
                
            }
            items = menuItemDAO.searchByName(name, category, isAvailable);
        }
        Platform.runLater(() -> {
            productList.getChildren().clear();
            for (MenuItem menuItem : items) {
                try {
                    FXMLLoader productCardLoader = new FXMLLoader(getClass().getResource("/com/example/view/ProductCard.fxml"));
                    Node productCardNode = productCardLoader.load();
                    ProductCardController productCardController = productCardLoader.getController();
                    productCardController.setProductController(this);
                    productCardController.setProduct(menuItem);
                    productList.getChildren().add(productCardNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showAddProductFormDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/AddProductForm.fxml"));
            Parent addProductForm = loader.load();
            AddProductFormController addProductFormController = loader.getController();
            addProductFormController.setProductController(this);
            Stage stage = new Stage();
            stage.setTitle("Add Product");
            stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/image/Java.png")));
            Scene scene = new Scene(addProductForm, 400, 620);
            stage.setScene(scene);
            stage.onCloseRequestProperty().set(event -> {
                addProductButton.setDisable(false);
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
