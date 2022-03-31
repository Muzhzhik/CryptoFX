package com.example.cryptofx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ResultsController {

    @FXML
    private Pane ResultPane;

    @FXML
    private Button okResultButton;

    @FXML
    private TextArea resultTextArea;

    @FXML
    void initialize() {

        resultTextArea.setText(CryptoController.encryptResult);

        okResultButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) okResultButton.getScene().getWindow();
            stage.close();
        });
    }

}
