package com.example.cryptofx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import com.example.cryptofx.analyser.Analyser;
import com.example.cryptofx.analyser.StatisticAnalysis;
import com.example.cryptofx.brutforce.Brutforce;
import com.example.cryptofx.brutforce.CaesarBrutfoce;
import com.example.cryptofx.cryptor.CaesarCryptor;
import com.example.cryptofx.cryptor.Cryptor;
import com.example.cryptofx.dao.DataDAO;
import com.example.cryptofx.dao.FileManagerDAO;
import com.example.cryptofx.utils.Action;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CryptoController {

    private Action currentAction;

    public static String encryptResult;

    public static String currentWarningMessage;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button brutforce_button;

    @FXML
    private Label brutforce_laber;

    @FXML
    private Button chooseFilePath1;

    @FXML
    private Button chooseFilePath2;

    @FXML
    private Label cryptoanalyse_laber;

    @FXML
    private Button cryptoanalysis_button;

    @FXML
    private Button decrypt_buttor;

    @FXML
    private Label decrypt_laber;

    @FXML
    private Button encrypt_button;

    @FXML
    private Label encrypt_laber;

    @FXML
    private Button exit_button;

    @FXML
    private Label fileForAnaluseLabel;

    @FXML
    private Label fileNameLaber;

    @FXML
    private TextField filePath2;

    @FXML
    private TextField filepath1;

    @FXML
    private CheckBox isSaveResult;

    @FXML
    private TextField keyForEncDec;

    @FXML
    private Label keyLabel;

    @FXML
    private TextField newFileName;

    @FXML
    private Button startButton;

    @FXML
    private Label warning_message_label;


    @FXML
    void initialize() {
        currentAction = Action.ENCRYPT;
        setActionPointer();

        encrypt_button.setOnAction(actionEvent -> {
            this.currentAction = Action.ENCRYPT;
            setActionPointer();
        });

        decrypt_buttor.setOnAction(actionEvent -> {
            this.currentAction = Action.DECRYPT;
            setActionPointer();
        });

        brutforce_button.setOnAction(actionEvent -> {
            this.currentAction = Action.BRUTEFORCE;
            setActionPointer();
        });

        cryptoanalysis_button.setOnAction(actionEvent -> {
            this.currentAction = Action.ANALYZE;
            setActionPointer();
        });

        startButton.setOnAction(actionEvent -> {
            if (checkFieldsAndData()) {
                if (currentAction == Action.ENCRYPT || currentAction == Action.DECRYPT) {
                    encryptDecryptFile();
                } else if (currentAction == Action.BRUTEFORCE) {
                    brutforce();
                } else if (currentAction == Action.ANALYZE) {
                    makeStatisticAnalyse();
                }
                showResultWindow();
                clearTextFields();
            }
        });


        exit_button.setOnAction(actionEvent -> {
            System.exit(1);
        });

        chooseFilePath1.setOnAction(actionEvent -> {
            File file = new FileChooser().showOpenDialog(new Stage());
            if (file != null) {
                filepath1.setText(file.getAbsolutePath());
            }
        });

        chooseFilePath2.setOnAction(actionEvent -> {
            File file = new FileChooser().showOpenDialog(new Stage());
            if (file != null) {
                filePath2.setText(file.getAbsolutePath());
            }
        });
    }

    private void clearTextFields() {
        filepath1.setText("");
        filePath2.setText("");
        newFileName.setText("");
        keyForEncDec.setText("");
        warning_message_label.setText("");
    }

    private void makeStatisticAnalyse() {
        Analyser analyser = new StatisticAnalysis();
        DataDAO encryptedData = new FileManagerDAO();
        DataDAO notEncrypteddData = new FileManagerDAO();
        String encryptedString = encryptedData.getData(filepath1.getText());
        String notEncryptedString = notEncrypteddData.getData(filePath2.getText());
        encryptResult = analyser.makeAnalyse(encryptedString, notEncryptedString);
        saveData();
    }

    private void showResultWindow() {
        try {
            Parent anotherRoot = FXMLLoader.load(getClass().getResource("results.fxml"));
            Stage anotherStage = new Stage();
            anotherStage.setTitle("Results");
            anotherStage.setScene(new Scene(anotherRoot, 600, 400));
            anotherStage.show();
        } catch (IOException e) {

        }
    }

    private void brutforce() {
        DataDAO fileManager = new FileManagerDAO();
        String data = fileManager.getData(filepath1.getText());
        Brutforce brutforce = new CaesarBrutfoce();
        encryptResult = brutforce.doBrutforce(data);
        saveData();
    }

    private void encryptDecryptFile() {
        DataDAO fileManager = new FileManagerDAO();
        String data = fileManager.getData(filepath1.getText());
        int key;
        try {
            key = Integer.parseInt(keyForEncDec.getText());
            Cryptor cryptor = new CaesarCryptor();
            if (currentAction == Action.ENCRYPT) {
                encryptResult = cryptor.encrypt(data, key);
            } else {
                encryptResult = cryptor.decrypt(data, key);
            }
            saveData();
        } catch (Exception e) {

        }
    }

    private void saveData() {
        if (isSaveResult.isSelected()) {
            String fileName = newFileName.getText();
            if (fileName.equals("")) {
                fileName = "No_name_file";
            }
            Path path = Path.of(filepath1.getText());
            DataDAO fileManager = new FileManagerDAO();
            fileManager.writeData(createNewPath(path, fileName), encryptResult);
        }
    }

    private String createNewPath(Path path, String fileName) {
        String directory = path.getParent().resolve(fileName).toString();
        String[] split = path.toString().split("\\.");
        String fileClass = split[split.length - 1];
        return directory + "." + fileClass;
    }

    private boolean checkFieldsAndData() {
        String message = null;
        if (filepath1.getText().trim().equals("")) {
            message = "Enter file name";
        } else if (currentAction == Action.ENCRYPT || currentAction == Action.DECRYPT) {
            if (keyForEncDec.getText().trim().equals("")) {
                message = "Enter key value";
            } else {
                try {
                    Integer.parseInt(keyForEncDec.getText());
                } catch (Exception e) {
                    message = "Key must to be integer";
                }
            }
        } else if (currentAction == Action.ANALYZE) {
            if (filePath2.getText().trim().equals("")) {
                message = "Enter file name for analyse";
            }
        }

        if (message != null) {
                warning_message_label.setText(message);
                warning_message_label.setVisible(true);
        }
        return message == null;
    }

    private void setActionPointer() {
        warning_message_label.setVisible(false);
        encrypt_laber.setVisible(false);
        decrypt_laber.setVisible(false);
        brutforce_laber.setVisible(false);
        cryptoanalyse_laber.setVisible(false);
        if (currentAction == Action.ENCRYPT) {
            encrypt_laber.setVisible(true);
            fileForAnaluseLabel.setVisible(false);
            filePath2.setVisible(false);
            chooseFilePath2.setVisible(false);
            keyForEncDec.setVisible(true);
            keyLabel.setVisible(true);
        } else if (currentAction == Action.DECRYPT) {
            decrypt_laber.setVisible(true);
            fileForAnaluseLabel.setVisible(false);
            filePath2.setVisible(false);
            chooseFilePath2.setVisible(false);
            keyForEncDec.setVisible(true);
            keyLabel.setVisible(true);
        } else if (currentAction == Action.BRUTEFORCE) {
            brutforce_laber.setVisible(true);
            fileForAnaluseLabel.setVisible(false);
            filePath2.setVisible(false);
            chooseFilePath2.setVisible(false);
            keyForEncDec.setVisible(false);
            keyLabel.setVisible(false);
        } else if (currentAction == Action.ANALYZE) {
            cryptoanalyse_laber.setVisible(true);
            fileForAnaluseLabel.setVisible(true);
            filePath2.setVisible(true);
            chooseFilePath2.setVisible(true);
            keyForEncDec.setVisible(false);
            keyLabel.setVisible(false);
        }
    }

}
