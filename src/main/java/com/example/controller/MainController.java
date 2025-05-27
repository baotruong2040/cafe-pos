package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.model.MenuItem;
import com.example.dao.MenuItemDAO;
import com.example.dao.OrderItemDAO;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {
    //navigation bar
    @FXML
    Circle logoCircle;
    @FXML
    Button dashboardNavButton;
    @FXML
    Button menuNavButton;
    @FXML
    Button productNavButton;
    @FXML
    Button settingNavButton;
    // main UI
    @FXML
    BorderPane MainUI;
    @FXML
    HBox menu;
    @FXML
    Button searchButton;
    @FXML
    TextField searchField;
    @FXML 
    FlowPane foodItems;
    @FXML
    VBox orderList;
    @FXML
    Button dineInButton;
    @FXML
    Button takeAwayButton;  
    @FXML
    Region hightLightRegion;
    @FXML
    Label subtotalPrice;
    @FXML
    Label taxPrice;
    @FXML
    Label totalPrice;
    @FXML
    Button clearButton;
    @FXML
    Button payButton;
    @FXML
    HBox allCategory;
    @FXML
    HBox cafeCategory;
    @FXML
    HBox traCategory;
    @FXML
    HBox banhNgotCategory;
    @FXML 
    HBox nuocEpCategory;
    @FXML
    Label allCategoryQuantity;
    @FXML
    Label cafeCategoryQuantity;
    @FXML
    Label traCategoryQuantity;
    @FXML
    Label banhNgotCategoryQuantity;
    @FXML
    Label nuocEpCategoryQuantity;
    @FXML
    Rectangle allCategoryImage;
    @FXML
    Rectangle cafeCategoryImage;
    @FXML
    Rectangle traCategoryImage;
    @FXML
    Rectangle banhNgotCategoryImage;
    @FXML
    Rectangle nuocEpCategoryImage;
    @FXML
    FlowPane mostOrderedItemList;
    @FXML
    ScrollPane scrollPane;

    ScrollPane productUI;
    ScrollPane dashboardUI;

    MenuItemDAO menuItemDAO = new MenuItemDAO();
    DineInController dineInController;
    TakeAwayController takeAwayController;
    ProductController productController;
    DashboardController dashboardController;
    List<MenuCardController> menuCardControllers = new ArrayList<>();
    String orderType = "DineIn";
    Button currentButton = dineInButton; //for highlighting the current button
    String currentCategory = "All"; //for filtering menu items by category
    Button currentNavigationButton;
    @FXML
    public void initialize() {
        loadMenuItemsToMenu(currentCategory);
        setLogo();
        loadMostOrderedItems();
        showNumberOfItems();
        loadUI();
        switchToDineInUI();
        currentNavigationButton = menuNavButton;
        dineInButton.getStyleClass().add("tab-button-chosen");
        menuNavButton.getStyleClass().add("nav-button-actived");
        allCategory.getStyleClass().add("category-button-chosen");
        butonSetOnAction();
    }

    protected void loadMenuItemsToMenu(String category){
        foodItems.getChildren().clear();
        List<MenuItem> menuItems;
        if (category.equalsIgnoreCase("All")) {
            menuItems = menuItemDAO.findByAvailability(true);
        
        }else {
            menuItems = menuItemDAO.findByCategoryAndAvailability(category, true);  
        }
        for (MenuItem item : menuItems) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/view/menuCard.fxml"));
                Node card = cardLoader.load();
                MenuCardController cardController = cardLoader.getController();
                cardController.setMenuItem(item);
                cardController.setMainController(this);
                menuCardControllers.add(cardController);
                foodItems.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setLogo() {
        new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    Image logoImage = new Image(getClass().getResourceAsStream("/com/example/image/logo.jpg"));
                    logoCircle.setFill(new javafx.scene.paint.ImagePattern(logoImage));
                } catch (Exception e) {
                    System.err.println("Logo image not found: " + e.getMessage());
                }
            });
        }).start();
    }

    private void switchToDineInUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/DineIn.fxml"));
            VBox dineInUI = loader.load();
            dineInController = loader.getController();
            dineInController.setMainController(this);
            orderList.getChildren().clear();
            orderList.getChildren().add(dineInUI);
            orderType = "DineIn";
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToTakeAwayUI() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/TakeAway.fxml"));
            VBox takeAwayUI = loader.load();
            takeAwayController = loader.getController();
            takeAwayController.setMainController(this);
            orderList.getChildren().clear();
            orderList.getChildren().add(takeAwayUI);
            orderType = "TakeAway";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTotalPrice(double price) {
        double tax = price * 0.08;
        double total = price + tax;
        if (price == 0) {
            subtotalPrice.setText(0 + " VNĐ");
            taxPrice.setText(0 + " VNĐ");
            totalPrice.setText(0 + " VNĐ");
            payButton.setText("Pay");
        }else {
            subtotalPrice.setText(String.format("%.3f VNĐ", price/1000));
            taxPrice.setText(String.format("%.3f VNĐ", tax/1000));
            totalPrice.setText(String.format("%.3f VNĐ", total/1000));
            payButton.setText(String.format("Pay %.3f VNĐ", total/1000));
        }
        
    }

    private void butonSetOnAction() {
        dineInButton.setOnAction(event -> {
            if (currentButton != dineInButton) {
                takeAwayButton.getStyleClass().remove("tab-button-chosen");
                dineInButton.getStyleClass().add("tab-button-chosen");
                takeAwayButton.setDisable(true);
                translateHighLight(dineInButton);
                switchToDineInUI();
                currentButton = dineInButton;

            }
        });
        takeAwayButton.setOnAction(event -> {
            if (currentButton != takeAwayButton) {
                dineInButton.getStyleClass().remove("tab-button-chosen");
                takeAwayButton.getStyleClass().add("tab-button-chosen");
                dineInButton.setDisable(true);
                translateHighLight(takeAwayButton);
                switchToTakeAwayUI();
                currentButton = takeAwayButton; 
            }
        });
        clearButton.setOnAction(event -> {
            handleClear();
        });

        allCategory.setOnMouseClicked(event -> {
            if (currentCategory != "All") {
                currentCategory = "All";
                loadMenuItemsToMenu(currentCategory);
                allCategory.getStyleClass().add("category-button-chosen");
                cafeCategory.getStyleClass().remove("category-button-chosen");
                traCategory.getStyleClass().remove("category-button-chosen");
                banhNgotCategory.getStyleClass().remove("category-button-chosen");
                nuocEpCategory.getStyleClass().remove("category-button-chosen");
            }
        });
        cafeCategory.setOnMouseClicked(event -> {
            if (currentCategory != "Cà Phê") {
                currentCategory = "Cà Phê";
                loadMenuItemsToMenu(currentCategory);
                allCategory.getStyleClass().remove("category-button-chosen");
                cafeCategory.getStyleClass().add("category-button-chosen");
                traCategory.getStyleClass().remove("category-button-chosen");
                banhNgotCategory.getStyleClass().remove("category-button-chosen");
                nuocEpCategory.getStyleClass().remove("category-button-chosen");
            }
        });
        traCategory.setOnMouseClicked(event -> {
            if (currentCategory != "Trà") {
                currentCategory = "Trà";
                loadMenuItemsToMenu(currentCategory);
                allCategory.getStyleClass().remove("category-button-chosen");
                cafeCategory.getStyleClass().remove("category-button-chosen");
                traCategory.getStyleClass().add("category-button-chosen");
                banhNgotCategory.getStyleClass().remove("category-button-chosen");
                nuocEpCategory.getStyleClass().remove("category-button-chosen");
            }
        });
        banhNgotCategory.setOnMouseClicked(event -> {
            if (currentCategory != "Bánh Ngọt") {
                currentCategory = "Bánh Ngọt";
                loadMenuItemsToMenu(currentCategory);
                allCategory.getStyleClass().remove("category-button-chosen");
                cafeCategory.getStyleClass().remove("category-button-chosen");
                traCategory.getStyleClass().remove("category-button-chosen");
                banhNgotCategory.getStyleClass().add("category-button-chosen");
                nuocEpCategory.getStyleClass().remove("category-button-chosen");
            }
        });
        nuocEpCategory.setOnMouseClicked(event -> {
            if (currentCategory != "Nước Ép") {
                currentCategory = "Nước Ép";
                loadMenuItemsToMenu(currentCategory);
                allCategory.getStyleClass().remove("category-button-chosen");
                cafeCategory.getStyleClass().remove("category-button-chosen");
                traCategory.getStyleClass().remove("category-button-chosen");
                banhNgotCategory.getStyleClass().remove("category-button-chosen");
                nuocEpCategory.getStyleClass().add("category-button-chosen");
            }
        });

        menuNavButton.setOnAction(event -> {
            if (currentNavigationButton != menuNavButton) {
                currentNavigationButton.getStyleClass().remove("nav-button-actived");
                menuNavButton.getStyleClass().add("nav-button-actived");
                translateHighLight(menuNavButton);
                setMenu();
                currentNavigationButton = menuNavButton;
                Stage stage = (Stage) MainUI.getScene().getWindow();
                stage.setFullScreen(true);

            }
        });
        dashboardNavButton.setOnAction(event -> {
            if (currentNavigationButton != dashboardNavButton) {
                currentNavigationButton.getStyleClass().remove("nav-button-actived");
                dashboardNavButton.getStyleClass().add("nav-button-actived");
                translateHighLight(dashboardNavButton);
                setDashboard();
                currentNavigationButton = dashboardNavButton;
            }
        });
        productNavButton.setOnAction(event -> {
            if (currentNavigationButton != productNavButton) {
                currentNavigationButton.getStyleClass().remove("nav-button-actived");
                productNavButton.getStyleClass().add("nav-button-actived");
                translateHighLight(productNavButton);
                setProduct();
                currentNavigationButton = productNavButton;
                Stage stage = (Stage) MainUI.getScene().getWindow();
                stage.setFullScreen(false);
            }
        });
        searchButton.setOnAction(event -> {
            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                handleSearch(searchText);
            } else {
                loadMenuItemsToMenu(currentCategory);
            }
        });
        
    }

    public void translateHighLight(Button button) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), hightLightRegion);
        if (button == dineInButton) {
            transition.setByX(-144);
            transition.setOnFinished(event -> {
                takeAwayButton.setDisable(false);
            });
        } else if (button == takeAwayButton) {
            transition.setByX(144);
            transition.setOnFinished(event -> {
                dineInButton.setDisable(false);
            });    
        }
        transition.setInterpolator(Interpolator.EASE_OUT);
        transition.setCycleCount(1);
        transition.setAutoReverse(false);
        transition.play();
    }

    private void loadUI() {
        try {
            FXMLLoader productLoader = new FXMLLoader(getClass().getResource("/com/example/view/ProductUI.fxml"));
            productUI = productLoader.load();
            productController = productLoader.getController();
            productController.setMainController(this);

            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/com/example/view/Dashboard.fxml"));
            dashboardUI = dashboardLoader.load();
            dashboardController = dashboardLoader.getController();
            dashboardController.setMainController(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMenu() {
        MainUI.setCenter(menu);
        
    }
    private void setProduct() {
        
        MainUI.setCenter(productUI);
    }
    private void setDashboard() {
        
        MainUI.setCenter(dashboardUI);
    }
    
    public void handleClear() {
        showTotalPrice(0);{}
        if (orderType.equals("DineIn")) {
            dineInController.clearCart();
            dineInController.orderedItems.clear();  
        }else {
            takeAwayController.clearCart();
            takeAwayController.orderedItems.clear();
        }
        for (MenuCardController controller : menuCardControllers) {
            controller.clearQuantity();
        }
    }

    private void handleSearch(String searchText) {
        foodItems.getChildren().clear();
        List<MenuItem> menuItems = menuItemDAO.searchByName(searchText, currentCategory, true);
        for (MenuItem item : menuItems) {
            try {
                FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/example/view/menuCard.fxml"));
                Node card = cardLoader.load();
                MenuCardController cardController = cardLoader.getController();
                cardController.setMenuItem(item);
                cardController.setMainController(this);
                foodItems.getChildren().add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showNumberOfItems() {
        new Thread(() -> {
            int allCount = menuItemDAO.getAllItemCount();
            int cafeCount = menuItemDAO.countItemByCategory("Cà Phê");
            int traCount = menuItemDAO.countItemByCategory("Trà");
            int banhNgotCount = menuItemDAO.countItemByCategory("Bánh Ngọt");
            int nuocEpCount = menuItemDAO.countItemByCategory("Nước Ép");
            Platform.runLater(() -> {
                allCategoryQuantity.setText(String.valueOf(allCount)+" sản phẩm");
                allCategoryImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/com/example/image/all.png"))));
                cafeCategoryQuantity.setText(String.valueOf(cafeCount)+" sản phẩm");
                cafeCategoryImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/com/example/image/cafe.png"))));
                traCategoryQuantity.setText(String.valueOf(traCount)+" sản phẩm");
                traCategoryImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/com/example/image/tea.png"))));
                banhNgotCategoryQuantity.setText(String.valueOf(banhNgotCount)+" sản phẩm");
                banhNgotCategoryImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/com/example/image/cake.png"))));
                nuocEpCategoryQuantity.setText(String.valueOf(nuocEpCount)+" sản phẩm");
                nuocEpCategoryImage.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("/com/example/image/juice.png"))));
            });
        }).start();
    }

    public void loadMostOrderedItems() {
        new Thread(() -> {
            OrderItemDAO orderedItemDAO = new OrderItemDAO();
            List<Object[]> mostOrderedItems = orderedItemDAO.findTop3MenuItems();
            Platform.runLater(() -> {
                for (Object[] objects : mostOrderedItems) {
                    MenuItem menuItem = (MenuItem) objects[0];
                    try {
                        FXMLLoader menuCardLoader = new FXMLLoader(getClass().getResource("/com/example/view/menuCard.fxml"));
                        Node menucard = menuCardLoader.load();                            
                        MenuCardController menuCardController = menuCardLoader.getController();
                        menuCardController.setMenuItem(menuItem);
                        menuCardController.setMainController(this);
                        menuCardControllers.add(menuCardController);
                        mostOrderedItemList.getChildren().add(menucard);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                    
                }
            });
        }).start();
    }
}
