package com.example.pipeburstclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.controlsfx.control.textfield.CustomTextField;

public class MainController {
    @FXML
    public Label messageInput;
    @FXML
    private CustomTextField lengthField;
    @FXML
    private CustomTextField diameterField;
    @FXML
    private CustomTextField horizontalQuantityField;
    @FXML
    private CustomTextField verticalQuantityField;

    @FXML
    private Button startInputPoints;

    @FXML
    private ListView<String> listView;

    @FXML
    public void inputPipeData() {

    }

    @FXML
    public void inputPointsData() {

    }



    private ObservableList<String> items;

    @FXML
    private void initialize() {
        items = FXCollections.observableArrayList("Item 1", "Item 2", "Item 3", "Item 4");
        listView.setItems(items);
        listView.setVisible(false);
        startInputPoints.setOnAction(this::inputPipeData);
    }

    private void inputPipeData(ActionEvent actionEvent) {
        String lengthText = lengthField.getText();
        String diameterText = diameterField.getText();
        String horizontalQuantityText = horizontalQuantityField.getText();
        String verticalQuantityText = verticalQuantityField.getText();

        if (lengthText.isEmpty() || diameterText.isEmpty() ||
                horizontalQuantityText.isEmpty() || verticalQuantityText.isEmpty()) {
            // Одно или более полей пустые
            // Вывести сообщение или выполнить другие действия

            messageInput.setText("ВЫ ВВЕЛИ ПУСТОЕ ПОЛЕ!");
            return;
        }

        try {
            double length = Double.parseDouble(lengthText);
            double diameter = Double.parseDouble(diameterText);
            int horizontalQuantity = Integer.parseInt(horizontalQuantityText);
            int verticalQuantity = Integer.parseInt(verticalQuantityText);
            //Pipe pipe = new Pipe(length, diameter, horizontalQuantity, verticalQuantity);
            startInputPoints.setOnAction(this::handleShowListButton);
            messageInput.setText("НАЧИНАЕТСЯ ВИЗУАЛИЗАЦИЯ В НОВОМ ОКНЕ!");
            // Вы можете использовать созданный объект pipe дальше
        } catch (NumberFormatException e) {

        }
    }

    private void handleShowListButton(ActionEvent event) {
        listView.setVisible(true);
        startInputPoints.setVisible(false);
    }
}
