package com.example.controller;

import javafx.fxml.FXML;
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
    @FXML
    public void initialize() {
        NumberAxis xAxis = new NumberAxis(1, 7, 1);
        xAxis.setLabel("Ngày");


        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Doanh thu (VNĐ)");

        LineChart<Number, Number> doanhThuLine = new LineChart<>(xAxis, yAxis);
        doanhThuLine.setTitle("Doanh thu theo ngày");


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu");

        series.getData().add(new XYChart.Data<>(1, 1500000));
        series.getData().add(new XYChart.Data<>(2, 1700000));
        series.getData().add(new XYChart.Data<>(3, 1600000));
        series.getData().add(new XYChart.Data<>(4, 1800000));
        series.getData().add(new XYChart.Data<>(5, 2000000));
        series.getData().add(new XYChart.Data<>(6, 2200000));
        series.getData().add(new XYChart.Data<>(7, 2500000));

        doanhThuLine.getData().add(series);
        lineChart.getChildren().add(doanhThuLine);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


}
