package com.example.rubank_ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class HelloController {

        @FXML
        private ToggleGroup Account;

        @FXML
        private ToggleGroup Campus;


        @FXML
        private DatePicker DOBLabel;


        @FXML
        private TextField firstname;

        @FXML
        private TextField lastname;

        @FXML
        private TextField amount;

        @FXML
        private CheckBox loyalCustomerCheckbox;

        static AccountDatabase accountDatabase = new AccountDatabase();

        @FXML
        void handleCollegeCheckingSelection() {

                loyalCustomerCheckbox.setDisable(true);
                loyalCustomerCheckbox.setSelected(false);

                // Enable the Campus radio buttons
                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(false);
                        }
                });
        }
        @FXML
        void handleOtherAccountSelection() {

                RadioButton selectedAccount = (RadioButton) Account.getSelectedToggle();
                String accountType = selectedAccount.getText();

                boolean enableLoyalCustomer = "Savings".equals(accountType) || "Money Market".equals(accountType);
                loyalCustomerCheckbox.setDisable(!enableLoyalCustomer);

                if (!enableLoyalCustomer) {
                        loyalCustomerCheckbox.setSelected(false);
                }

                Campus.getToggles().forEach(toggle -> {
                        if (toggle instanceof RadioButton) {
                                ((RadioButton) toggle).setDisable(true);
                                toggle.setSelected(false);

                        }
                });
        }

        @FXML
        protected void openAccount() {
                RadioButton selectedRadioButton = (RadioButton) Account.getSelectedToggle();
                String selectedAccountType = selectedRadioButton.getText();

                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String dateString = DOBLabel.getValue().toString();
                System.out.println(dateString);
                Date date = Date.fromDateStr(dateString);

                if (date.isValid() && date.isFutureDate()) {
                        int age = date.calculateAge();
                        Account account = null;
                        switch (selectedAccountType) {
                                case "Checking":
                                        if (age >= 16) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        if (deposit > 0) {
                                                                Profile profile = new Profile(firstName, lastName, date);
                                                                account = new Checking(profile, deposit);
                                                        } else {
                                                                System.out.println("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        System.out.println("Not a valid amount.");
                                                }
                                        } else {
                                                System.out.println("DOB invalid: " + dateString + " under 16.");
                                        }
                                        break;

                                case "Money Market":
                                        if (age >= 16) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        if (deposit > 0) {
                                                                if (deposit >= 2000) {
                                                                        Profile profile = new Profile(firstName, lastName, date);
                                                                        int withdrawal = 0;
                                                                        account = new MoneyMarket(profile, deposit, true, withdrawal);
                                                                } else {
                                                                        System.out.println("Minimum of $2000 to open a Money Market account.");
                                                                }
                                                        } else {
                                                                System.out.println("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        System.out.println("Not a valid amount.");
                                                }
                                        } else {
                                                System.out.println("DOB invalid: " + dateString + " under 16.");
                                        }
                                        break;

                                case "Savings":
                                        if (age >= 16) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        boolean isLoyal = loyalCustomerCheckbox.isSelected();
                                                        if (deposit > 0) {
                                                                Profile profile = new Profile(firstName, lastName, date);
                                                                account = new Savings(profile, deposit, isLoyal);

                                                                if (accountDatabase.contains(account)) {
                                                                        System.out.println("Account already exists in the database.");
                                                                        break;
                                                                }
                                                        } else {
                                                                System.out.println("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        System.out.println("Not a valid amount.");
                                                }
                                        } else {
                                                System.out.println("DOB invalid: " + dateString + " under 16.");
                                        }
                                        break;

                                case "College Checking":
                                        if (age >= 16 && age <= 24) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        RadioButton selectedRadioButtonforCampus = (RadioButton) Campus.getSelectedToggle();
                                                        String campusName= selectedRadioButtonforCampus.getText();

                                                        Campus campus = null;
                                                        switch (campusName) {
                                                                case "NB":
                                                                        campus = com.example.rubank_ui.Campus.NEW_BRUNSWICK;
                                                                        break;
                                                                case "Newark":
                                                                        campus = com.example.rubank_ui.Campus.NEWARK;
                                                                        break;
                                                                case "Camden":
                                                                        campus = com.example.rubank_ui.Campus.CAMDEN;
                                                                        break;
                                                                default:
                                                                        System.out.println("Invalid campus code.");
                                                                        break;
                                                        }
                                                        if (campus != null) {
                                                                if (deposit > 0) {
                                                                        Profile profile = new Profile(firstName, lastName, date);
                                                                        account = new CollegeChecking(profile, deposit, campus);
                                                                } else {
                                                                        System.out.println("Initial deposit cannot be 0 or negative.");
                                                                }
                                                        }
                                                } catch (NumberFormatException e) {
                                                        System.out.println("Not a valid amount.");
                                                }
                                        } else {
                                                if (age < 16) {
                                                        System.out.println("DOB invalid: " + dateString + " under 16.");
                                                } else {
                                                        System.out.println("DOB invalid: " + dateString + " over 24.");
                                                }
                                        }
                                        break;

                                default:
                                        System.out.println("Invalid account type.");
                        }

                        if (account != null) {
                                if (accountDatabase.open(account)) {
                                        System.out.println(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ")" + " opened.");
                                } else {
                                        System.out.println(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ")" + " is already in the database.");
                                }
                        }
                } else {
                        System.out.println("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.");
                }
        }
}
