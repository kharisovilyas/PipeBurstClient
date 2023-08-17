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

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x, y);
        //LineChart<Number, Number> numberLineChart = new LineChart<Number,Number>(x, y);
        numberScatterChart.setTitle("Series");

        XYChart.Series<Number, Number> series1 = new XYChart.Series();
        XYChart.Series<Number, Number> series2 = new XYChart.Series();
        //XYChart.Series series3 = new XYChart.Series();


        Scene scene = new Scene(numberScatterChart, 600, 600);

        series1.setName("Флюид");
        series2.setName("Стенка Трубопровода");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        //ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();

        double d = pipe.getDiameter();
        double l = pipe.getLength();
        double verticalQuantity = pipe.getVerticalQuantity()+1;
        double horizontalQuantity = pipe.getHorizontalQuantity();
        double dd = d / verticalQuantity;
        double dl = l / horizontalQuantity;
        for (int i = 0; i <= verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {
                XYChart.Data symbol = new XYChart.Data(j*dl,i*dd);

                if (i == 0||i == verticalQuantity) {
                    //pipe.getPoints().get(i).get(j).density
                    series2.getData().add(new XYChart.Data<>(j*dl,i*dd));
                    // series2.setNode(new CustomDataNode(getColorBasedOnValue(pipe.getPoints().get(i).get(j).density)));
                }
                else {
                    series1.getData().add(new XYChart.Data<>(j*dl,i*dd));
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
            double density1 = pipe.getPoints().get((int) ((int) yValue/dd)).get((int) ((int) xValue/dl)).density;

            Color pointColor = getColorBasedOnValue(density1); // Change this based on your criteria
            dataPoint.setNode(new CustomDataNode(pointColor));



        }

        //series3.setData(datas3);

        //vBox.getChildren().add(numberScatterChart);
        //vBox.getChildren().add(numberScatterChart);




        scene.getOnScrollStarted();

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

    private Color getColorBasedOnValue(double density) {
        // Implement your logic to determine color based on the value
        // For example, you can use gradients, thresholds, or custom mappings
        // Here, we'll simply use a gradient from blue to red based on value
        if (density<=1000 && density > 950){
            return Color.rgb(255,0,0);
        }
        else if (density<=950 && density>900){
            return Color.rgb(255,102,0);
        }
        else {
            return Color.rgb(0,0,255);
        }
    }

    private class CustomDataNode extends javafx.scene.shape.Circle {
        public CustomDataNode(Color color) {
            setRadius(5);
            setFill(color);
            setStroke(Color.BLACK);
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}