package com.example.pipeburstclient;


import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ParameterInputDialog extends Dialog<ButtonType> {

    private TextField lengthField;
    private TextField diameterField;
    private TextField horizontalQuantityField;
    private TextField verticalQuantityField;

    public ParameterInputDialog() {
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

        grid.add(new Label("Length:"), 0, 0);
        grid.add(lengthField, 1, 0);
        grid.add(new Label("Diameter:"), 0, 1);
        grid.add(diameterField, 1, 1);
        grid.add(new Label("Horizontal Quantity:"), 0, 2);
        grid.add(horizontalQuantityField, 1, 2);
        grid.add(new Label("Vertical Quantity:"), 0, 3);
        grid.add(verticalQuantityField, 1, 3);

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
