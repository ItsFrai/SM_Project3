package com.example.rubank_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {

        @FXML
        private ToggleGroup Campus;

        @FXML
        private TextArea mainOutput;

        @FXML
        private Label firstNameLabel;

        @FXML
        private Label lastNameLabel;

        @FXML
        private DatePicker DOBLabel;

        @FXML
        private Label AmountLabel;

        @FXML
        private Button openButton;
        @FXML
        void handleCollegeCheckingSelection(ActionEvent event) {
                // Enable the Campus radio buttons
                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(false);
                        }
                });
        }
        @FXML
        void handleOtherAccountSelection(ActionEvent event) {
                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(true);
                        }
                });
        }

}
