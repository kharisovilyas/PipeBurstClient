package com.example.pipeburstclient;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PointInputController{
    @FXML
    private Label titleLabel;

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
