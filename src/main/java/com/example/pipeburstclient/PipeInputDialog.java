package com.example.pipeburstclient;


import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class PipeInputDialog extends Dialog<ButtonType> {

    private TextField lengthField;
    private TextField diameterField;
    private TextField horizontalQuantityField;
    private TextField verticalQuantityField;

    private RadioButton waterRadioButton = new RadioButton("Water");
    private RadioButton oilRadioButton = new RadioButton("Oil");
    private ToggleGroup fluidToggleGroup = new ToggleGroup();
    public PipeInputDialog() {
        setTitle("Enter Parameters");
        setHeaderText("Enter parameters for visualization:");

        ButtonType confirmButtonType = new ButtonType("Confirm");
        getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        lengthField = new TextField();
        diameterField = new TextField();
        horizontalQuantityField = new TextField();
        verticalQuantityField = new TextField();

        grid.add(new Label("Pipes Length:"), 0, 0);
        grid.add(lengthField, 1, 0);
        grid.add(new Label("Pipes Diameter:"), 0, 1);
        grid.add(diameterField, 1, 1);
        grid.add(new Label("Horizontal Quantity of points:"), 0, 2);
        grid.add(horizontalQuantityField, 1, 2);
        grid.add(new Label("Vertical Quantity of points:"), 0, 3);
        grid.add(verticalQuantityField, 1, 3);
        // Создайте флюиды



        // Добавьте флюиды в группу
        waterRadioButton.setToggleGroup(fluidToggleGroup);
        oilRadioButton.setToggleGroup(fluidToggleGroup);

        // Add RadioButtons to the GridPane
        grid.add(new Label("Choice of fluid:"), 0, 4);
        grid.add(waterRadioButton, 1, 4);
        grid.add(oilRadioButton, 1, 5);

        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return ButtonType.OK;
            }
            return ButtonType.CANCEL;
        });
    }

    public double getLength() {
        return Double.parseDouble(lengthField.getText());
    }

    public double getDiameter() {
        return Double.parseDouble(diameterField.getText());
    }

    public int getHorizontalQuantity() {
        return Integer.parseInt(horizontalQuantityField.getText());
    }

    public int getVerticalQuantity() {
        return Integer.parseInt(verticalQuantityField.getText());
    }
}
