package com.example.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.example.dao.MenuItemDAO;
import com.example.model.MenuItem;
import com.example.util.ConvertString;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddProductFormController {
    @FXML
    private ComboBox<String> StatusAdd;

    @FXML
    private Button addImageButton;

    @FXML
    private Button addNewProductButton;

    @FXML
    private ImageView addProductImage;

    @FXML
    private Button cancelProductButton;

    @FXML
    Button updateProductButton;
    @FXML
    private ComboBox<String> categoryAdd;

    @FXML
    private TextField nameAddField;

    @FXML
    private TextField priceAddField;

    File imageFile;
    String imagePath;
    ProductController productController;
    MenuItem updateItem;

    @FXML
    public void initialize() {
        
        categoryAdd.getItems().addAll("Cà Phê", "Bánh Ngọt", "Trà", "Nước Ép");
        StatusAdd.getItems().addAll("Available", "Unavailable");

        StatusAdd.setValue("Available");

        addImageButton.setOnAction(event -> {
            imageFile = getImageFile();
            if (imageFile != null) {
                addProductImage.setImage(new Image(imageFile.toURI().toString()));
            }
        });

        cancelProductButton.setOnAction(event -> {
            Stage stage = (Stage) cancelProductButton.getScene().getWindow();
            productController.addProductButton.setDisable(false);
            stage.close();
        });

        addNewProductButton.setOnAction(event -> {
            handleAdd();
        });

        updateProductButton.setOnAction(event -> {
            handleUpdate();
        });
    }

    private File getImageFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(addImageButton.getScene().getWindow());
        if (selectedFile != null && selectedFile.exists()) {
            return selectedFile;
        } else {
            return null;
        }
    }

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    public void setProductItem(MenuItem item) {
        updateProductButton.setVisible(true);
        addNewProductButton.setVisible(false);
        if (item != null) {
            this.updateItem = item;
            nameAddField.setText(item.getName());
            priceAddField.setText(String.valueOf(item.getPrice()));
            categoryAdd.setValue(item.getCategory());
            StatusAdd.setValue(item.isAvailable() ? "Available" : "Unavailable");
            try {
                addProductImage.setImage(new Image(getClass().getResourceAsStream(item.getImagePath())));
                imageFile = new File ("src/main/resources/"+item.getImagePath());
            } catch (Exception e) {
                addProductImage.setImage(new Image(getClass().getResourceAsStream("/com/example/image/placeHolder.png")));
            }
        }

    }

    public void handleAdd() {
        String name = nameAddField.getText().trim();
            double price;
            try {
                price = Double.parseDouble(priceAddField.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price");
                alert.setContentText("Please enter a valid number for price.");
                alert.showAndWait();
                return;
            }
            String category = categoryAdd.getValue();
            boolean isAvailable = StatusAdd.getValue().equals("Available");
            if (name.isEmpty() || price <= 0 || category == null || category.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please fill in all fields correctly.");
                alert.showAndWait();
                return;
            }
            
            if (imageFile != null) {
                String imageName = ConvertString.convert(name);
                String destinationPath = "src/main/resources/com/example/image/" + imageName + ".png";
                imagePath = "/com/example/image/" + imageName + ".png";
                try {
                    Files.copy(imageFile.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to Save Image");
                    alert.setContentText("Could not save the image file.");
                    alert.showAndWait();
                    return;
                }
            }
            MenuItem newItem = new MenuItem();
            newItem.setName(name);
            newItem.setPrice(price);
            newItem.setCategory(category);
            newItem.setAvailable(isAvailable);
            newItem.setImagePath(imagePath);

            MenuItemDAO menuItemDAO = new MenuItemDAO();
            try {
                menuItemDAO.save(newItem);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Product Added Successfully");
                alert.setContentText("The product has been added to the menu.");
                alert.showAndWait();
                productController.loadProducts("", productController.selectedCategory, productController.selectedStatus);
                productController.addProductButton.setDisable(false);
                Stage stage = (Stage) addNewProductButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Add Product");
                alert.setContentText("An error occurred while adding the product. Please try again.");
                alert.showAndWait();    
            }
    }

    public void handleUpdate() {
        String name = nameAddField.getText().trim();
            double price;
            try {
                price = Double.parseDouble(priceAddField.getText().trim());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Price");
                alert.setContentText("Please enter a valid number for price.");
                alert.showAndWait();
                return;
            }
            String category = categoryAdd.getValue();
            boolean isAvailable = StatusAdd.getValue().equals("Available");
            if (name.isEmpty() || price <= 0 || category == null || category.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please fill in all fields correctly.");
                alert.showAndWait();
                return;
            }
            if (imageFile != null) {
                String imageName = ConvertString.convert(name);
                String destinationPath = "src/main/resources/com/example/image/" + imageName + ".png";
                imagePath = "/com/example/image/" + imageName + ".png";
                try {
                    Files.copy(imageFile.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to Save Image");
                    alert.setContentText("Could not save the image file.");
                    alert.showAndWait();
                    return;
                }
            } else {
                imagePath = updateItem.getImagePath();
            }
            updateItem.setName(name);
            updateItem.setPrice(price);
            updateItem.setCategory(category);
            updateItem.setAvailable(isAvailable);
            updateItem.setImagePath(imagePath);
            MenuItemDAO menuItemDAO = new MenuItemDAO();
            try {
                menuItemDAO.update(updateItem);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Product Updated Successfully");
                alert.setContentText("The product has been updated in the menu.");
                alert.showAndWait();
                productController.loadProducts("", productController.selectedCategory, productController.selectedStatus);
                Stage stage = (Stage) updateProductButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Update Product");
                alert.setContentText("An error occurred while updating the product. Please try again.");
                alert.showAndWait();
            }
    }
}
