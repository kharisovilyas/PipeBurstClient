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
        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x, y);
        Active_computing active_computing = new Active_computing();
        Pane newPane = new Pane();

        //LineChart<Number, Number> numberLineChart = new LineChart<Number,Number>(x, y);
        numberScatterChart.setTitle("Series");

        XYChart.Series<Number, Number> series1 = new XYChart.Series();
        XYChart.Series<Number, Number> series2 = new XYChart.Series();
        //XYChart.Series series3 = new XYChart.Series();

        vBox.getChildren().addAll(numberScatterChart, active_computing.getGrid_computing());

        Scene scene = new Scene(vBox, 600, 600);

        series1.setName("Флюид");
        series2.setName("Стенка Трубопровода");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        //ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();

        double d = pipe.getDiameter();
        double l = pipe.getLength();
        double verticalQuantity = pipe.getVerticalQuantity() + 1;
        double horizontalQuantity = pipe.getHorizontalQuantity();
        double dd = d / verticalQuantity;
        double dl = l / horizontalQuantity;
        for (int i = 0; i <= verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {
                XYChart.Data symbol = new XYChart.Data(j * dl, i * dd);

                if (i == 0 || i == verticalQuantity) {
                    //pipe.getPoints().get(i).get(j).density
                    series2.getData().add(new XYChart.Data<>(j * dl, i * dd));
                    // series2.setNode(new CustomDataNode(getColorBasedOnValue(pipe.getPoints().get(i).get(j).density)));
                } else {
                    series1.getData().add(new XYChart.Data<>(j * dl, i * dd));
                }
            }

        }

        for (int i = 0; i <= verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {

            }
        }

        for (XYChart.Data<Number, Number> dataPoint : series1.getData()) {
            double xValue = dataPoint.getXValue().doubleValue();
            double yValue = dataPoint.getYValue().doubleValue();
            pipe.Properties_with_Burst(75);
            double density1 = pipe.getPoints().get((int) ((int) yValue / dd)).get((int) ((int) xValue / dl)).density;

            Color pointColor = getColorBasedOnValue(density1, 1000); // Change this based on your criteria
            dataPoint.setNode(new CustomDataNode(pointColor));


        }

        //series3.setData(datas3);

        //vBox.getChildren().add(numberScatterChart);


        numberScatterChart.getData().add(series1);
        numberScatterChart.getData().add(series2);
        //numberScatterChart.getData().add(series3);
        //System.out.println(numberScatterChart.getData().get(0).getData());
        //ScrollPane scrollPane = new ScrollPane(numberLineChart);
        for (XYChart.Series<Number, Number> s : numberScatterChart.getData()) {
            for (XYChart.Data<Number, Number> d1 : s.getData()) {
                Tooltip.install(d1.getNode(), new Tooltip(
                        d1.getXValue().toString() + "\n" +
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
        if (currentDensity>=0.9*startDensity) {
            return Color.rgb(251, 63, 65);
        } else if (currentDensity>=0.86*startDensity && currentDensity < 0.9*startDensity) {
            return Color.rgb(227, 110, 57);
        } else if (currentDensity>=0.82*startDensity && currentDensity < 0.86*startDensity) {
            return Color.rgb(250, 183, 74);
        }else if (currentDensity>=0.78*startDensity && currentDensity < 0.82*startDensity) {
            return Color.rgb(227, 199, 57);
        }else if (currentDensity>=0.74*startDensity && currentDensity < 0.78*startDensity) {
            return Color.rgb(217, 255, 61);
        } else if (currentDensity>=0.7*startDensity && currentDensity < 0.74*startDensity) {
            return Color.rgb(75, 227, 64);
        }else if (currentDensity>=0.66*startDensity && currentDensity < 0.7*startDensity) {
            return Color.rgb(82, 250, 175);
        }else if (currentDensity>=0.62*startDensity && currentDensity < 0.66*startDensity) {
            return Color.rgb(64, 203, 227);
        }else if (currentDensity>=0.58*startDensity && currentDensity < 0.62*startDensity) {
            return Color.rgb(78, 129, 252);}
        else {
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