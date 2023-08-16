package com.example.pipeburstclient;

import com.example.pipeburstclient.engine.model.Pipe;
import javafx.fxml.FXML;
import org.controlsfx.control.textfield.CustomTextField;

public class MainController {
    @FXML
    private CustomTextField lengthField;
    @FXML
    private CustomTextField diameterField;
    @FXML
    private CustomTextField horizontalQuantityField;
    @FXML
    private CustomTextField verticalQuantityField;

    @FXML
    public void inputPipeData() {
        String lengthText = lengthField.getText();
        String diameterText = diameterField.getText();
        String horizontalQuantityText = horizontalQuantityField.getText();
        String verticalQuantityText = verticalQuantityField.getText();

        if (lengthText.isEmpty() || diameterText.isEmpty() ||
                horizontalQuantityText.isEmpty() || verticalQuantityText.isEmpty()) {
            // Одно или более полей пустые
            // Вывести сообщение или выполнить другие действия

            // Подсветить текстовые поля красным
            if (lengthText.isEmpty()) {
                diameterField.getStyleClass().add("red-border");
            }
            if (diameterText.isEmpty()) {
                diameterField.getStyleClass().add("red-border");
            }
            if (horizontalQuantityText.isEmpty()) {
                horizontalQuantityField.getStyleClass().add("red-border");
            }
            if (verticalQuantityText.isEmpty()) {
                verticalQuantityField.getStyleClass().add("red-border");
            }

            return;
        }

        try {
            double length = Double.parseDouble(lengthText);
            double diameter = Double.parseDouble(diameterText);
            int horizontalQuantity = Integer.parseInt(horizontalQuantityText);
            int verticalQuantity = Integer.parseInt(verticalQuantityText);

            Pipe pipe = new Pipe(length, diameter, horizontalQuantity, verticalQuantity);
            inputPointsData();
            // Вы можете использовать созданный объект pipe дальше
        } catch (NumberFormatException e) {

        }
    }

    @FXML
    public void inputPointsData() {

    }

    @FXML
    private void startVisualization(){

    }
}
