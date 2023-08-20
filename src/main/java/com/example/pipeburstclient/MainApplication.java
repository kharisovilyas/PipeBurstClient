package com.example.pipeburstclient;


import com.example.pipeburstclient.engine.model.Burst;
import com.example.pipeburstclient.engine.model.Pipe;
import com.example.pipeburstclient.engine.model.Point;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Pipe Burst Visualisation");
        PipeInputDialog dialog = new PipeInputDialog();
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) {
            double length = dialog.getLength();
            double diameter = dialog.getDiameter();
            int horizontalQuantity = dialog.getHorizontalQuantity();
            int verticalQuantity = dialog.getVerticalQuantity();
            Point generationSyntacticData = new Point(
                    1.5,
                    0,
                    500_000,
                    90,
                    1000,
                    false
            );

            List<Point> listPoints = new ArrayList<>();
            for (int i = 0; i < verticalQuantity; i++) {
                listPoints.add(generationSyntacticData);
            }
            Pipe pipe = new Pipe(
                    length,
                    diameter,
                    horizontalQuantity,
                    verticalQuantity,
                    listPoints
            );
            pipe.calculationNormalPipePoints(listPoints);
            pipe.calculationBurstPipePoints(750, 0.6);
            List<Double> pipeDiameters = new ArrayList<>();
            List<Double> sensorsSteps = new ArrayList<>();

// Создаем значения для pipeDiameters от 0.003 до 1, увеличивая на 0.01
            for (int i = 10; i <= 100; i+=10) {  // Увеличиваем на 1, а затем делим на 100
                double value = i * 0.001;
                pipeDiameters.add(value);
            }

// Создаем значения для sensorsSteps от 1 до 50, увеличивая на 10
            for (int i = 1; i <= 50; i ++) {
                sensorsSteps.add((double) i);
            }

            Burst burst = new Burst(pipe);
            burst.optimumSensorPlacement(
                    pipeDiameters,
                    sensorsSteps
            );
            // Создайте содержимое для второго окна
            Label label = new Label("Это другое окно.");
            Scene secondaryScene = new Scene(label, 400, 200);

            primaryStage.setScene(secondaryScene);

            tableSensorsPlaces(primaryStage, burst, pipeDiameters, sensorsSteps);
            //charting(primaryStage, pipe);
        }

    }

    private void tableSensorsPlaces(Stage primaryStage, Burst burst, List<Double> pipeDiameters, List<Double> sensorsSteps) {
        // Создаем TableView и устанавливаем столбцы
        TableView<Map<String, String>> tableView = new TableView<>();

        // Создаем столбец с диаметрами
        TableColumn<Map<String, String>, String> diameterColumn = new TableColumn<>("Diameter / Step Sensor");
        diameterColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get("Diameter / Step Sensor")));

        tableView.getColumns().add(diameterColumn);

        // Создаем столбцы с шагами
        for (Double sensorStep : sensorsSteps) {
            TableColumn<Map<String, String>, String> sensorColumn = new TableColumn<>(String.valueOf(sensorStep));
            sensorColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().get(sensorStep.toString())));

            tableView.getColumns().add(sensorColumn);
        }

        // Создаем данные для таблицы
        ObservableList<Map<String, String>> data = FXCollections.observableArrayList();

        for (int i = 0; i < pipeDiameters.size(); i++) {
            Map<String, String> row = new HashMap<>();

            row.put("Diameter / Step Sensor", String.valueOf(pipeDiameters.get(i)));

            for (int j = 0; j < sensorsSteps.size(); j++) {
                String sensorDataString = buildStringFromMap(burst.getDeltas().get(i).get(j));
                row.put(sensorsSteps.get(j).toString(), sensorDataString);
            }

            data.add(row);
        }

        tableView.setItems(data);

        VBox vbox = new VBox(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private String buildStringFromMap(Map<String, Double> deltaMap) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("ΔTemperature: ").append(deltaMap.get("Temperature")).append("\n");
        stringBuilder.append("ΔVelocity: ").append(deltaMap.get("Velocity")).append("\n");
        stringBuilder.append("ΔPressure: ").append(deltaMap.get("Pressure")).append("\n");
        stringBuilder.append("ΔDensity: ").append(deltaMap.get("Density")).append("\n");
        stringBuilder.append("ΔFlow: ").append(deltaMap.get("Flow"));

        return stringBuilder.toString();
    }

    private void charting(Stage primaryStage, Pipe pipe) {
        NumberAxis x1 = new NumberAxis();
        NumberAxis y1 = new NumberAxis();
        NumberAxis x2 = new NumberAxis();
        NumberAxis y2 = new NumberAxis();
        NumberAxis x3 = new NumberAxis();
        NumberAxis y3 = new NumberAxis();
        NumberAxis x4 = new NumberAxis();
        NumberAxis y4 = new NumberAxis();
        ScatterChart<Number, Number> DensityScatterChart = new ScatterChart<Number, Number>(x1, y1);
        ScatterChart<Number, Number> VelocityScatterChart = new ScatterChart<Number, Number>(x2, y2);
        ScatterChart<Number, Number> PressureScatterChart = new ScatterChart<Number, Number>(x3, y3);
        ScatterChart<Number, Number> TemperatureScatterChart = new ScatterChart<Number, Number>(x4, y4);

        //LineChart<Number, Number> numberLineChart = new LineChart<Number,Number>(x, y);
        DensityScatterChart.setTitle("Плотность");
        VelocityScatterChart.setTitle("Скорость");
        PressureScatterChart.setTitle("Давление");
        TemperatureScatterChart.setTitle("Температура");


        XYChart.Series<Number, Number> seriesDensity = new XYChart.Series();
        XYChart.Series<Number, Number> seriesVelocity = new XYChart.Series();
        XYChart.Series<Number, Number> seriesPressure = new XYChart.Series();
        XYChart.Series<Number, Number> seriesTemperature = new XYChart.Series();
        XYChart.Series<Number, Number> series_wallDensity = new XYChart.Series();
        XYChart.Series<Number, Number> series_wallPressure = new XYChart.Series();
        XYChart.Series<Number, Number> series_wallVelocity = new XYChart.Series();
        XYChart.Series<Number, Number> series_wallTemperature = new XYChart.Series();
        //XYChart.Series series3 = new XYChart.Series();


        Button openAnotherWindowButton = new Button("Sensor pitch table");
        openAnotherWindowButton.setOnAction(event -> openAnotherWindow(pipe));
        // Добавляем кнопку в сцену
        VBox vbox = new VBox(openAnotherWindowButton);
        vbox.getChildren().addAll(DensityScatterChart, VelocityScatterChart, PressureScatterChart, TemperatureScatterChart);
        Scene scene = new Scene(vbox, 600, 600);


        double d = pipe.getDiameter();
        double l = pipe.getLength();
        double verticalQuantity = pipe.getVerticalQuantity();
        double horizontalQuantity = pipe.getHorizontalQuantity();
        double dd = d / verticalQuantity;
        double dl = l / horizontalQuantity;
        for (int i = 1; i < verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {
                seriesDensity.getData().add(new XYChart.Data<>(j * dl, i * dd));
                seriesVelocity.getData().add(new XYChart.Data<>(j * dl, i * dd));
                seriesPressure.getData().add(new XYChart.Data<>(j * dl, i * dd));
                seriesTemperature.getData().add(new XYChart.Data<>(j * dl, i * dd));
            }

        }


        for (int k = 0; k < horizontalQuantity; k++) {
            series_wallDensity.getData().add(new XYChart.Data<>(k * dl, 0));
            series_wallDensity.getData().add(new XYChart.Data<>(k * dl, verticalQuantity * dd));
            series_wallPressure.getData().add(new XYChart.Data<>(k * dl, 0));
            series_wallPressure.getData().add(new XYChart.Data<>(k * dl, verticalQuantity * dd));
            series_wallVelocity.getData().add(new XYChart.Data<>(k * dl, 0));
            series_wallVelocity.getData().add(new XYChart.Data<>(k * dl, verticalQuantity * dd));
            series_wallTemperature.getData().add(new XYChart.Data<>(k * dl, 0));
            series_wallTemperature.getData().add(new XYChart.Data<>(k * dl, verticalQuantity * dd));
        }
        pipe.calculationBurstPipePoints(750, 0.6);

        for (XYChart.Data<Number, Number> dataPoint : seriesDensity.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            double density1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).density;
            Color pointColor_density = getColorBasedOnValue(density1, 1000); // Change this based on your criteria

            dataPoint.setNode(new CustomDataNode(pointColor_density));
        }
        for (XYChart.Data<Number, Number> dataPoint : seriesVelocity.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            double velocity1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).velocity;
            Color pointColor_velocity = getColorBasedOnValue(velocity1, 1.5); // Change this based on your criteria
            dataPoint.setNode(new CustomDataNode(pointColor_velocity));
        }
        for (XYChart.Data<Number, Number> dataPoint : seriesPressure.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            double pressure1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).pressure;
            Color pointColor_pressure = getColorBasedOnValue(pressure1,500000); // Change this based on your criteria

            dataPoint.setNode(new CustomDataNode(pointColor_pressure));

        }
        for (XYChart.Data<Number, Number> dataPoint : seriesTemperature.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            double temperature1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).temperature;
            Color pointColor_temperature = getColorBasedOnValue(temperature1, 90); // Change this based on your criteria

            dataPoint.setNode(new CustomDataNode(pointColor_temperature));

        }
        DensityScatterChart.getData().add(seriesDensity);
        DensityScatterChart.getData().add(series_wallDensity);
        VelocityScatterChart.getData().add(seriesVelocity);
        VelocityScatterChart.getData().add(series_wallVelocity);
        PressureScatterChart.getData().add(seriesPressure);
        PressureScatterChart.getData().add(series_wallPressure);
        TemperatureScatterChart.getData().add(seriesTemperature);
        TemperatureScatterChart.getData().add(series_wallTemperature);
        for (XYChart.Series<Number, Number> s : DensityScatterChart.getData()) {
            for (XYChart.Data<Number, Number> d1 : s.getData()) {
                Tooltip.install(d1.getNode(), new Tooltip(
                        "\n" +
                                "Number Of Events : " + d1.getYValue().toString()));

                //Adding class on hover
                d1.getNode().setOnMouseEntered(event -> d1.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d1.getNode().setOnMouseExited(event -> d1.getNode().getStyleClass().remove("onHover"));
            }
        }


        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private Color getColorBasedOnValue(double currentDensity, double startDensity) {
        if (currentDensity >= 0.9 * startDensity) {
            return Color.rgb(251, 63, 65);
        } else if (currentDensity >= 0.86 * startDensity && currentDensity < 0.9 * startDensity) {
            return Color.rgb(227, 110, 57);
        } else if (currentDensity >= 0.82 * startDensity && currentDensity < 0.86 * startDensity) {
            return Color.rgb(250, 183, 74);
        } else if (currentDensity >= 0.78 * startDensity && currentDensity < 0.82 * startDensity) {
            return Color.rgb(227, 199, 57);
        } else if (currentDensity >= 0.74 * startDensity && currentDensity < 0.78 * startDensity) {
            return Color.rgb(217, 255, 61);
        } else if (currentDensity >= 0.7 * startDensity && currentDensity < 0.74 * startDensity) {
            return Color.rgb(75, 227, 64);
        } else if (currentDensity >= 0.66 * startDensity && currentDensity < 0.7 * startDensity) {
            return Color.rgb(82, 250, 175);
        } else if (currentDensity >= 0.62 * startDensity && currentDensity < 0.66 * startDensity) {
            return Color.rgb(64, 203, 227);
        } else if (currentDensity >= 0.58 * startDensity && currentDensity < 0.62 * startDensity) {
            return Color.rgb(78, 129, 252);
        } else {
            return Color.rgb(0, 0, 255);
        }
    }

    private void openAnotherWindow(Pipe pipe) {
        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Другое окно");

        // Отобразите второе окно
        secondaryStage.show();
    }

    private class CustomDataNode extends javafx.scene.shape.Circle {
        public CustomDataNode(Color color) {
            setRadius(5);
            setFill(color);
            setStroke(color);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}