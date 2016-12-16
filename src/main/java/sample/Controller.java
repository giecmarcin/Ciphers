package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import services.*;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    final ObservableList<String> algorithmsItems = FXCollections
            .observableArrayList("Caesar cipher", "Hill", "Fence cipher", "Beaufort", "MD5");

    final ObservableList<String> codeOrDecodeItems = FXCollections
            .observableArrayList("Code", "Decode");
    @FXML
    private CheckBox checkBoxM22;

    @FXML
    private CheckBox checkBoxM33;
    @FXML
    private TextField mTwo11;

    @FXML
    private TextField mTwo22;

    @FXML
    private TextField mThree11;

    @FXML
    private TextField mThree22;

    @FXML
    private TextField mThree33;

    @FXML
    private TextField mTwo21;

    @FXML
    private TextField mThree21;

    @FXML
    private TextField mThree32;

    @FXML
    private TextField mThree13;
    @FXML
    private TextField mThree12;

    @FXML
    private TextField mThree23;

    @FXML
    private TextField mThree31;

    @FXML
    private TextField mTwo12;

    @FXML
    private Button btnConvert;

    @FXML
    private TextArea textAreaTarget;

    @FXML
    private TextArea textAreaSource;

    @FXML
    private TextField tFKey;

    @FXML
    private ComboBox<String> comboBoxAlgorithm;

    @FXML
    private ComboBox<String> comboxCodeOrDecode;


    public void initialize(URL location, ResourceBundle resources) {
        comboBoxAlgorithm.setItems(algorithmsItems);
        comboxCodeOrDecode.setItems(codeOrDecodeItems);
        comboxCodeOrDecode.getSelectionModel().select(0);
        try {
            configureButtons();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.show();
        }

        iniatilizeMatrix2x2();
        tFKey.setText("3");
        comboBoxAlgorithm.getSelectionModel().select(0);
        configureComboBoxListeners();


    }

    public void configureComboBoxListeners() {
        comboBoxAlgorithm.valueProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (comboBoxAlgorithm.getSelectionModel().getSelectedIndex() == 1) {
                    checkBoxM22.setSelected(true);
                } else {
                    checkBoxM22.setSelected(false);
                }
            }
        });
    }

    private void configureButtons() {
        btnConvert.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //System.out.println(textAreaSource.getText());
                String choosenAlgorithm = comboBoxAlgorithm.getSelectionModel().getSelectedItem();
                //0 - code text
                //1 - decode text
                int action = comboxCodeOrDecode.getSelectionModel().getSelectedIndex();
                if (choosenAlgorithm.equals("MD5")) {
                    System.out.println("Md5");
                    final String iv = "0123456789123456"; // This has to be 16 characters
                    final String secretKey = "AES";
                    Md5 md5 = new Md5();
                    if (action == 0) {
                        try {
                            String encryptedData = md5.encrypt(textAreaSource.getText());
                            textAreaTarget.setText(encryptedData);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Not supported!");
                        alert.show();
                    }
                } else if (choosenAlgorithm.equals("Caesar cipher")) {
                    Caesar caesar = new Caesar();
                    int key = Integer.parseInt(tFKey.getText());
                    if (action == 0) {
                        String encryptedData = caesar.caesar(textAreaSource.getText(), key);
                        textAreaTarget.setText(encryptedData);
                    } else {
                        String originalText = caesar.caesar(textAreaSource.getText(), -key);
                        textAreaTarget.setText(originalText);
                    }
                } else if (choosenAlgorithm.equals("Hill")) {
                    Hill hill = new Hill();
                    if (action == 0) {
                        //encrypt
                        if (checkBoxM22.isSelected()) {
                            double[][] matrixKey = fromGuiToMatrix2x();
                            try {
                                String encryptedText = hill.encrypt(textAreaSource.getText(), matrixKey, 2);
                                textAreaTarget.setText(encryptedText);
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            }

                        } else if (checkBoxM33.isSelected()) {

                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Choose matrix type", ButtonType.OK);
                            alert.show();
                        }
                    } else {
                        //decrypt
                        if (checkBoxM22.isSelected()) {
                            double[][] matrixKey = fromGuiToMatrix2x();
                            try {
                                String originalText = hill.decrypt(textAreaSource.getText(), matrixKey, 2);
                                textAreaTarget.setText(originalText);
                            } catch (Exception e) {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            }

                        } else if (checkBoxM33.isSelected()) {

                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Choose matrix type", ButtonType.OK);
                            alert.show();
                        }

                    }
                } else if (choosenAlgorithm.equals("Fence cipher")) {
                    int key = Integer.parseInt(tFKey.getText());
                    Fence fence = new Fence(key);
                    if (action == 0) {
                        String encryptedData = fence.encrypt(textAreaSource.getText().replaceAll("\\s+", "")); //remove space
                        textAreaTarget.setText(encryptedData);
                    } else {
                        String originalText = fence.decode(textAreaSource.getText());
                        textAreaTarget.setText(originalText);
                    }

                } else if (choosenAlgorithm.equals("Beaufort")) {
                    Beaufort beaufort = new Beaufort();
                    int key = Integer.parseInt(tFKey.getText());
                    if (action == 0) {
                        String encryptedData = beaufort.encryptOrDecrypt(textAreaSource.getText(), key);
                        textAreaTarget.setText(encryptedData);
                    } else {
                        String originalText = beaufort.encryptOrDecrypt(textAreaSource.getText(), key);
                        textAreaTarget.setText(originalText);
                    }
                } else {

                }
            }
        });
    }

    private void iniatilizeMatrix2x2() {
        // double[][] matrixKey = {{1d, 2d}, {2d, 5d}}; //key
        mTwo11.setText("1");
        mTwo12.setText("2");
        mTwo21.setText("2");
        mTwo22.setText("5");
    }

    private double[][] fromGuiToMatrix2x() {
        double[][] matrixKey = {{Double.parseDouble(mTwo11.getText()), Double.parseDouble(mTwo12.getText())},
                {Double.parseDouble(mTwo21.getText()), Double.parseDouble(mTwo22.getText())}}; //key
        return matrixKey;
    }
}
