package com.example.pipeburstclient;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class PointsInputDialog extends Pane {
    private TextField lengthField_computing;
    private TextField diameterField_computing;
    private TextField horizontalQuantityField_computing;
    private TextField verticalQuantityField_computing;
    private TextField density_computing;

    private TextField pressure_computing;
    private TextField velocity_computing;
    private TextField flow_computing;
    private TextField temperature_computing;

    private GridPane grid_computing;


    public PointsInputDialog() {
        //ButtonType confirmButton_computing = new ButtonType("Confirm");

        grid_computing = new GridPane();
        grid_computing.setHgap(10);
        grid_computing.setVgap(10);
        lengthField_computing = new TextField();
        diameterField_computing = new TextField();
        horizontalQuantityField_computing = new TextField();
        verticalQuantityField_computing = new TextField();
        density_computing = new TextField();
        pressure_computing = new TextField();
        velocity_computing = new TextField();
        flow_computing = new TextField();
        temperature_computing = new TextField();

        grid_computing.add(new Label("Length:"), 0, 0);
        grid_computing.add(lengthField_computing, 1, 0);
        grid_computing.add(new Label("Diameter:"), 0, 1);
        grid_computing.add(diameterField_computing, 1, 1);
        grid_computing.add(new Label("Horizontal Quantity:"), 0, 2);
        grid_computing.add(horizontalQuantityField_computing, 1, 2);
        grid_computing.add(new Label("Vertical Quantity:"), 0, 3);
        grid_computing.add(verticalQuantityField_computing, 1, 3);
        grid_computing.add(new Label("Density:"), 0, 4);
        grid_computing.add(density_computing, 1, 4);
        grid_computing.add(new Label("Pressure:"), 0, 5);
        grid_computing.add(pressure_computing, 1, 5);
        grid_computing.add(new Label("Velocity:"), 0, 6);
        grid_computing.add(velocity_computing, 1, 6);
        grid_computing.add(new Label("Flow:"), 0, 7);
        grid_computing.add(flow_computing, 1, 7);
        grid_computing.add(new Label("Temperature:"), 0, 8);
        grid_computing.add(temperature_computing, 1, 8);


    }


    public TextField getLengthFieldComputing() {
        return lengthField_computing;
    }

    public TextField getDiameterFieldComputing() {
        return diameterField_computing;
    }

    public TextField getHorizontalQuantityFieldComputing() {
        return horizontalQuantityField_computing;
    }

    public TextField getVerticalQuantityFieldComputing() {
        return verticalQuantityField_computing;
    }

    public TextField getDensityComputing() {
        return density_computing;
    }

    public TextField getPressureComputing() {
        return pressure_computing;
    }

    public TextField getVelocityComputing() {
        return velocity_computing;
    }

    public TextField getFlowComputing() {
        return flow_computing;
    }

    public TextField getTemperatureComputing() {
        return temperature_computing;
    }

    public GridPane getGridComputing() {
        return grid_computing;
    }



}