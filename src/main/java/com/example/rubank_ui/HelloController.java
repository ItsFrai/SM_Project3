package com.example.rubank_ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class HelloController {

        @FXML
        private ToggleGroup Campus;
        @FXML
        public void handleCollegeCheckingSelection() {
                // Enable the Campus radio buttons
                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(false);
                        }
                });
        }
        @FXML
        public void handleOtherAccountSelection() {
                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(true);
                        }
                });
        }

        public void handleOpenAccount(ActionEvent event){



        }
}
