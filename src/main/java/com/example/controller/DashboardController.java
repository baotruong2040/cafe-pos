package com.example.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.dao.MenuItemDAO;
import com.example.dao.OrderDAO;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {
    @FXML
    private Label itemsChange;

    @FXML
    private Label itemsStat;

    @FXML
    private VBox lineChart;

    @FXML
    private Label moneyPercent;

    @FXML
    private Label moneyStat;

    @FXML
    private Label orderPercent;

    @FXML
    private Label orderStat;

    @FXML
    private VBox pieChart;

    @FXML
    private Label weekOrderPercent;

    @FXML
    private Label weekOrderStat;
    MainController mainController;
    MenuItemDAO menuItemDAO = new MenuItemDAO();
    OrderDAO orderDAO = new OrderDAO();

    int itemCount;
    double dailyRevenue;
    List<Object[]> result;
    
    @FXML
    public void initialize() {
        loadLineChart();
        setLabel();
    }
    public void refresh() {
        loadLineChart();
        setLabel();
    }
    public void loadLineChart() {
        lineChart.getChildren().clear();
        new Thread(() -> {
            Platform.runLater(() -> {
                result = orderDAO.getWeeklyRevenue();
                CategoryAxis xAxis = new CategoryAxis();
                xAxis.setLabel("Date");

                NumberAxis yAxis = new NumberAxis();
                yAxis.setLabel("Revenue (VND)");

                LineChart<String, Number> revenueChart = new LineChart<>(xAxis, yAxis);
                revenueChart.setTitle("Weekly Revenue Overview");
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Daily Revenue");

                for (Object[] row : result) {
                    String dateStr;
                    if (row[0] instanceof java.sql.Date) {
                        // chuyen java.sql.Date sang LocalDate
                        dateStr = ((java.sql.Date) row[0]).toLocalDate().toString();
                    } else if (row[0] instanceof LocalDate) {
                        dateStr = ((LocalDate) row[0]).toString();
                    } else {
                        // truong hop khac (String)
                        dateStr = row[0].toString();
                    }
                    
                    Number totalValue;
                    if (row[1] instanceof BigDecimal) {
                        totalValue = (BigDecimal) row[1];
                    } else if (row[1] instanceof Double) {
                        totalValue = (Double) row[1];
                    } else {
                        totalValue = Double.parseDouble(row[1].toString());
                    }
                    
                    series.getData().add(new XYChart.Data<>(dateStr, totalValue));
                }

                revenueChart.getData().add(series);

                lineChart.getChildren().add(revenueChart);
            });
        }).start();
    }

    public void setLabel() {
        new Thread(() -> {
            long orderCount = orderDAO.getDailyOrderCount();
            long orderweekcount = orderDAO.getWeeklyOrderCount();
            double dailyRevenue = orderDAO.getDailyRevenue();
            int itemCount = menuItemDAO.getAllItemCount();
            Platform.runLater(() -> {
                orderStat.setText(String.valueOf(orderCount));
                weekOrderStat.setText(String.valueOf(orderweekcount));
                moneyStat.setText(String.format("%,.0f VNĐ", dailyRevenue));
                itemsStat.setText(String.valueOf(itemCount));
            });
        }).start();

    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void loadPieChart() {
        
    }
}
