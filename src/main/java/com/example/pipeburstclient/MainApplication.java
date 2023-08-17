package com.example.pipeburstclient;



import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApplication extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        /*FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Pipe Burst");
        stage.setScene(scene);
        stage.show();*/


        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("sample.fxml"));
        primaryStage.setTitle("JavaFX Chart (Series)");

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();

        ScatterChart<Number, Number> numberScatterChart = new ScatterChart<Number, Number>(x, y);
        //LineChart<Number, Number> numberLineChart = new LineChart<Number,Number>(x, y);
        numberScatterChart.setTitle("Series");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        //XYChart.Series series3 = new XYChart.Series();

        series1.setName("Флюид");
        series2.setName("Стенка Трубопровода");

        ObservableList<XYChart.Data> datas = FXCollections.observableArrayList();
        ObservableList<XYChart.Data> datas2 = FXCollections.observableArrayList();
        //ObservableList<XYChart.Data> datas3 = FXCollections.observableArrayList();

        double d = 15;
        double l = 200;
        double verticalQuantity = 10 + 1;
        double horizontalQuantity = 20;
        double dd = d / verticalQuantity;
        double dl = l / horizontalQuantity;
        for (int i = 0; i <= verticalQuantity; i++) {
            for (int j = 0; j < horizontalQuantity; j++) {
                if (i == 0) {
                    datas2.add(new XYChart.Data(j * dl, 0));
                } else if (i == verticalQuantity) {
                    datas2.add(new XYChart.Data(j * dl, 1 * d));
                } else {
                    datas.add(new XYChart.Data(j * dl, i * dd));
                }
            }

        }

        series1.setData(datas);
        series2.setData(datas2);
        //series3.setData(datas3);

        //vBox.getChildren().add(numberScatterChart);
        //vBox.getChildren().add(numberScatterChart);


        Scene scene = new Scene(numberScatterChart, 600, 600);

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
        public static void main(String[] args) {
            launch(args);
        }
    }
//Tooltip tooltip1 = new Tooltip("12312312");
//tooltip1.install();
        /*ObservableList<XYChart.Data> dataList = ((XYChart.Series) numberScatterChart.getData().get(0)).getData();
        dataList.forEach(data->{
            Node node = data.getNode();
            Tooltip tooltip = new Tooltip('('+data.getXValue().toString()+';'+data.getYValue().toString()+')');
            tooltip.install(node, tooltip);
        });


/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToolTipOnLineChart extends Application {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void start(Stage stage) throws ParseException {
        stage.setTitle("Line Chart Sample");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Date");
        yAxis.setLabel("Events");

        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis, yAxis);
        lineChart.setTitle("Events");




        XYChart.Series<Number,Number> series = new XYChart.Series<>();
        series.setName("Events this Year");
        series.getData().add(new XYChart.Data(13, 23));
        series.getData().add(new XYChart.Data(15, 14));
        series.getData().add(new XYChart.Data(19, 15));
        series.getData().add(new XYChart.Data(128, 24));
        series.getData().add(new XYChart.Data(13, 34));
        series.getData().add(new XYChart.Data(16, 36));
        series.getData().add(new XYChart.Data(17, 22));
        series.getData().add(new XYChart.Data(12, 45));
        series.getData().add(new XYChart.Data(11, 43));
        series.getData().add(new XYChart.Data(12, 17));
        series.getData().add(new XYChart.Data(134, 29));
        series.getData().add(new XYChart.Data(138, 25));


        Scene scene  = new Scene(lineChart,800,600);
        //scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
        lineChart.getData().add(series);
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip(
                        d.getXValue().toString() + "\n" +
                                "Number Of Events : " + d.getYValue()));

                //Adding class on hover

            }
        }
        stage.setScene(scene);
        stage.show();

        /**
         * Browsing through the Data and applying ToolTip
         * as well as the class on hover
         */

