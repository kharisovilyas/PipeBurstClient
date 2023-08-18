package com.example.pipeburstclient;


import com.example.pipeburstclient.engine.model.Pipe;
import com.example.pipeburstclient.engine.model.Point;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        /*FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Pipe Burst");
        stage.setScene(scene);
        stage.show();*/


        //FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sample.fxml"));
        primaryStage.setTitle("Pipe Burst Visualisation");
        ParameterInputDialog dialog = new ParameterInputDialog();
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
            pipe.calculatePoints(listPoints);
            charting(primaryStage, pipe);
        }


    }

    private void charting(Stage primaryStage, Pipe pipe) {
        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        VBox vBox = new VBox();
        ScatterChart<Number, Number> DensityScatterChart = new ScatterChart<Number, Number>(x, y);
        ScatterChart<Number, Number> VelocityScatterChart = new ScatterChart<Number, Number>(x, y);
        ScatterChart<Number, Number> PressureScatterChart = new ScatterChart<Number, Number>(x, y);
        ScatterChart<Number, Number> TemperatureScatterChart = new ScatterChart<Number, Number>(x, y);


        Active_computing active_computing = new Active_computing();
        Pane newPane = new Pane();

        //LineChart<Number, Number> numberLineChart = new LineChart<Number,Number>(x, y);
        DensityScatterChart.setTitle("Series");

        XYChart.Series<Number, Number> series1 = new XYChart.Series();
        XYChart.Series<Number, Number> series2 = new XYChart.Series();
        //XYChart.Series series3 = new XYChart.Series();

        vBox.getChildren().addAll(DensityScatterChart, VelocityScatterChart, PressureScatterChart, TemperatureScatterChart);

        Scene scene = new Scene(vBox, 600, 600);

        series1.setName("Флюид");
        series2.setName("Стенка Трубопровода");


        double d = pipe.getDiameter();
        double l = pipe.getLength();
        double verticalQuantity = pipe.getVerticalQuantity();
        double horizontalQuantity = pipe.getHorizontalQuantity();
        double dd = d / verticalQuantity;
        double dl = l / horizontalQuantity;
        for (int i = 1; i < verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {
                XYChart.Data symbol = new XYChart.Data(j * dl, i * dd);
                series1.getData().add(new XYChart.Data<>(j * dl, i * dd));
            }

        }


        for (int k = 0; k < horizontalQuantity; k++) {
            series2.getData().add(new XYChart.Data<>(k * dl, 0));
            series2.getData().add(new XYChart.Data<>(k * dl,verticalQuantity*dd ));
        }
        pipe.Properties_with_Burst(750);

        for (XYChart.Data<Number, Number> dataPoint : series1.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            double density1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).density;
            double velocity1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).velocity;
            double pressure1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).pressure;
            double temperature1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).temperature;
            Color pointColor_density = getColorBasedOnValue(density1, 1000); // Change this based on your criteria
            Color pointColor_velocity = getColorBasedOnValue(velocity1, 1.5); // Change this based on your criteria
            Color pointColor_pressure = getColorBasedOnValue(pressure1, 500000); // Change this based on your criteria
            Color pointColor_temperature = getColorBasedOnValue(temperature1, 90); // Change this based on your criteria

            dataPoint.setNode(new CustomDataNode(pointColor_density));
            dataPoint.setNode(new CustomDataNode(pointColor_velocity));
            dataPoint.setNode(new CustomDataNode(pointColor_pressure));
            dataPoint.setNode(new CustomDataNode(pointColor_temperature));


        }

        //series3.setData(datas3);

        //vBox.getChildren().add(DensityScatterChart);


        DensityScatterChart.getData().add(series1);
        DensityScatterChart.getData().add(series2);
        VelocityScatterChart.getData().add(series1);
        VelocityScatterChart.getData().add(series2);
        PressureScatterChart.getData().add(series1);
        PressureScatterChart.getData().add(series2);
        TemperatureScatterChart.getData().add(series1);
        TemperatureScatterChart.getData().add(series2);
        //DensityScatterChart.getData().add(series3);
        //System.out.println(DensityScatterChart.getData().get(0).getData());
        //ScrollPane scrollPane = new ScrollPane(numberLineChart);
        for (XYChart.Series<Number, Number> s : DensityScatterChart.getData()) {
            for (XYChart.Data<Number, Number> d1 : s.getData()) {
                Tooltip.install(d1.getNode(), new Tooltip(
                        //pipe.getPoints().get((int) ((int) d1.getYValue().doubleValue() / dd)).get((int) ((int) d1.getYValue().doubleValue() / dl)).density+
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
        // Implement your logic to determine color based on the value
        // For example, you can use gradients, thresholds, or custom mappings
        // Here, we'll simply use a gradient from blue to red based on value
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