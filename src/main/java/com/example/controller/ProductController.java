package com.example.controller;

import java.util.List;

import com.example.dao.MenuItemDAO;
import com.example.model.MenuItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class ProductController {
    @FXML
    private ComboBox<String> StatusBox;

    @FXML
    private Button addProductButton;

    @FXML
    private ComboBox<String> categoryBox;

    @FXML
    private FlowPane productList;

    @FXML
    private TextField productSearchField;

    @FXML
    private Button refreshButton;

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

    private void loadProducts(String name, String category, String status) {
        productList.getChildren().clear();
        if (name.equals("") && category.equalsIgnoreCase("All") && status.equalsIgnoreCase("All")) {
            List<MenuItem> items = menuItemDAO.findAll();
            for (MenuItem menuItem : items) {
                try {
                    FXMLLoader productCardLoader = new FXMLLoader(getClass().getResource("/com/example/view/ProductCard.fxml"));
                    Node productCardNode = productCardLoader.load();
                    ProductCardController productCardController = productCardLoader.getController();
                    productCardController.setMainController(this);
                    productCardController.setProduct(menuItem);
                    productList.getChildren().add(productCardNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            boolean isAvailable;
            if (status.equalsIgnoreCase("Available")) {
                isAvailable = true;
            } else if (status.equalsIgnoreCase("Unavailable")) {
                isAvailable = false;
            } else {
                isAvailable = true;
                
            }
            List<MenuItem> items = menuItemDAO.searchByName(name, category, isAvailable);
            for (MenuItem menuItem : items) {
                try {
                    FXMLLoader productCardLoader = new FXMLLoader(getClass().getResource("/com/example/view/ProductCard.fxml"));
                    Node productCardNode = productCardLoader.load();
                    ProductCardController productCardController = productCardLoader.getController();
                    productCardController.setMainController(this);
                    productCardController.setProduct(menuItem);
                    productList.getChildren().add(productCardNode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
