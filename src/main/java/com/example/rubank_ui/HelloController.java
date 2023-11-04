package com.example.rubank_ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;


public class HelloController {

        public TextArea mainOutput;
        public TextArea deposit_output;
        public TextArea outputTextArea;

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
        protected void clearAllFields() {
                firstname.clear();
                lastname.clear();
                DOBLabel.getEditor().clear();
                amount.clear();
                Account.selectToggle(null);
                Campus.selectToggle(null);
                loyalCustomerCheckbox.setSelected(false);
        }
        @FXML
        private void showAlert(String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        @FXML
        protected void openAccount() {
                boolean errorEncountered = false;

                RadioButton selectedRadioButton = (RadioButton) Account.getSelectedToggle();
                String selectedAccountType = null;
                if (selectedRadioButton != null) {
                        selectedAccountType = selectedRadioButton.getText();
                } else {
                        showAlert("Missing data for opening an account.");
                        errorEncountered = true;
                }

                String firstName = firstname.getText();

                if (firstName == null || firstName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String lastName = lastname.getText();
                if (lastName == null || lastName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String dateString = DOBLabel.getValue() != null ? DOBLabel.getValue().toString() : null;
                Date date = null;
                if (dateString != null) {
                        date = Date.fromDateStr(dateString);
                } else {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                        }
                }
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
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount or missing deposit amount.");
                                                }
                                        } else {
                                                showAlert("DOB invalid: " + dateString + " under 16.");
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
                                                                        showAlert("Minimum of $2000 to open a Money Market account.");
                                                                }
                                                        } else {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                        } else {
                                                showAlert("DOB invalid: " + dateString + " under 16.");
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
                                                                        showAlert("Account already exists in the database.");
                                                                        break;
                                                                }
                                                        } else {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                        }
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                        } else {
                                                showAlert("DOB invalid: " + dateString + " under 16.");
                                        }
                                        break;

                                case "College Checking":
                                        if (age >= 16 && age <= 24) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        RadioButton selectedRadioButtonforCampus = (RadioButton) Campus.getSelectedToggle();
                                                        String campusName = selectedRadioButtonforCampus.getText();

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
                                                                        showAlert("Invalid campus code.");
                                                                        break;
                                                        }
                                                        if (campus != null) {
                                                                if (deposit > 0) {
                                                                        Profile profile = new Profile(firstName, lastName, date);
                                                                        account = new CollegeChecking(profile, deposit, campus);
                                                                } else {
                                                                        showAlert("Initial deposit cannot be 0 or negative.");
                                                                }
                                                        }
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                        } else {
                                                if (age < 16) {
                                                        showAlert("DOB invalid: " + dateString + " under 16.");
                                                } else {
                                                        showAlert("DOB invalid: " + dateString + " over 24.");
                                                }
                                        }
                                        break;

                                default:
                                        showAlert("Invalid account type.");
                        }

                        if (account != null) {
                                if (accountDatabase.open(account)) {
                                        mainOutput.appendText(firstName + " " + lastName + " " + dateString + " (" + account.short_AccountType() + ")" + " opened.\n");
                                } else {
                                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.\n");
                                }
                        }
                } else {
                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.");
                }
        }

        @FXML
        protected void closeAccount() {
                boolean errorEncountered = false;

                RadioButton selectedRadioButton = (RadioButton) Account.getSelectedToggle();
                String selectedAccountType = null;
                if (selectedRadioButton != null) {
                        selectedAccountType = selectedRadioButton.getText();
                } else {
                        showAlert("Missing data for opening an account.");
                        errorEncountered = true;
                }

                String firstName = firstname.getText();

                if (firstName == null || firstName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String lastName = lastname.getText();
                if (lastName == null || lastName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String dateString = DOBLabel.getValue() != null ? DOBLabel.getValue().toString() : null;
                Date date = null;
                if (dateString != null) {
                        date = Date.fromDateStr(dateString);
                } else {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                        }
                }
                if (date.isFutureDate()) {

                        Profile profile = new Profile(firstName, lastName, date);

                        Account accountToClose = switch (selectedAccountType) {
                                case "Checking" -> new Checking(profile, 0.0);
                                case "Money Market" -> new MoneyMarket(profile, 0.0, true, 0);
                                case "Savings" -> new Savings(profile, 0.0, false);
                                case "College Checking" -> new CollegeChecking(profile, 0.0, com.example.rubank_ui.Campus.NEW_BRUNSWICK);
                                default -> null;
                        };

                        if (accountToClose != null) {
                                if (accountDatabase.close(accountToClose)) {
                                        mainOutput.appendText(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ")" + " has been closed.\n");
                                } else {
                                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.\n");
                                }
                        } else {
                                showAlert("Invalid account type: " + selectedAccountType);
                        }
                } else {
                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.");
                }
        }

        @FXML
        protected void depositAccount() {
                RadioButton selectedRadioButton = (RadioButton) Account.getSelectedToggle();
                String selectedAccountType = selectedRadioButton.getText();

                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String dateString = DOBLabel.getValue().toString();
                Date date = Date.fromDateStr(dateString);

                if (date.isFutureDate()) {
                        double depositAmount;
                        try {
                                depositAmount = Double.parseDouble(amount.getText());
                        } catch (NumberFormatException e) {
                                showAlert("Not a valid amount.\n");
                                return;
                        }

                        if (depositAmount <= 0.0) {
                                showAlert("Deposit - amount cannot be 0 or negative.\n");
                                return;
                        }

                        Profile profile = new Profile(firstName, lastName, date);

                        Account shellAccount = switch (selectedAccountType) {
                                case "Checking" -> new Checking(profile, depositAmount);
                                case "Money Market" -> new MoneyMarket(profile, depositAmount, true, 0);
                                case "Savings" -> new Savings(profile, depositAmount, false);
                                case "College Checking" -> new CollegeChecking(profile, depositAmount, com.example.rubank_ui.Campus.NEW_BRUNSWICK);
                                default -> null;
                        };
                        String depositMessage = accountDatabase.deposit(shellAccount);
                        if (shellAccount.getBalance() == 0) {
                                showAlert(depositMessage);
                        } else {
                                deposit_output.appendText(depositMessage);
                        }
                } else {
                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.\n");
                }
        }

        @FXML
        protected void withdrawalAccount() {
                RadioButton selectedRadioButton = (RadioButton) Account.getSelectedToggle();
                String selectedAccountType = selectedRadioButton.getText();

                String firstName = firstname.getText();
                String lastName = lastname.getText();
                String dateString = DOBLabel.getValue().toString();
                Date date = Date.fromDateStr(dateString);


                if (date.isFutureDate()) {
                        double withdrawalAmount;
                        try {
                                withdrawalAmount = Double.parseDouble(amount.getText());
                        } catch (NumberFormatException e) {
                                showAlert("Not a valid amount.");
                                return;
                        }

                        if (withdrawalAmount <= 0.0) {
                                showAlert("Withdrawal - amount cannot be 0 or negative.");
                                return;
                        }

                        Profile profile = new Profile(firstName, lastName, date);

                        Account shellAccount = switch (selectedAccountType) {
                                case "Checking" -> new Checking(profile, withdrawalAmount);
                                case "Money Market" -> new MoneyMarket(profile, withdrawalAmount, true, 0);
                                case "Savings" -> new Savings(profile, withdrawalAmount, false);
                                case "College Checking" -> new CollegeChecking(profile, withdrawalAmount, com.example.rubank_ui.Campus.NEW_BRUNSWICK);
                                default -> null;
                        };

                        boolean accountExists = false;
                        for (int i = 0; i < accountDatabase.getNumAcct(); i++) {
                                if (accountDatabase.getAccounts()[i].equals(shellAccount, 5)) {
                                        accountExists = true;
                                        boolean withdrawalStatus = accountDatabase.withdraw(shellAccount);
                                        if (withdrawalStatus) {
                                                if (selectedAccountType.equals("Money Market")) {
                                                        MoneyMarket moneyMarketAccount = (MoneyMarket) accountDatabase.getAccounts()[i];
                                                        moneyMarketAccount.increaseWithdrawal();
                                                }
                                                deposit_output.appendText(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ") Withdrawal - balance updated.\n");
                                        } else {
                                                deposit_output.appendText(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ") Withdrawal - insufficient funds.\n");
                                        }
                                }
                        }

                        if (!accountExists) {
                                showAlert(firstName + " " + lastName + " " + dateString + " (" + selectedAccountType + ") is not in the database.\n");
                        }
                } else {
                        showAlert("DOB invalid: " + dateString + " not a valid calendar date or cannot be today or a future day.\n");
                }
        }

        @FXML
        protected void printSortedAccounts() {
                String sortedAccounts = accountDatabase.printSorted();
                outputTextArea.appendText(sortedAccounts);
        }

        @FXML
        protected void printFeesAndInterests() {
                String output = accountDatabase.printFeesAndInterests();
                outputTextArea.appendText(output);
        }

        @FXML
        protected void printUpdatedBalances() {
                String output = accountDatabase.printUpdatedBalances();
                outputTextArea.appendText(output);
        }

        @FXML
        protected void load_accounts() throws FileNotFoundException {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(new Stage());

                if (selectedFile != null) {
                        Scanner scanner = new Scanner(selectedFile);
                        while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                StringTokenizer tokenizer = new StringTokenizer(line, ",");

                                if (tokenizer.countTokens() < 5) {
                                        showAlert("Missing data for opening an account.");
                                        break;
                                }
                                String accountType = tokenizer.nextToken();
                                String firstName = tokenizer.nextToken();
                                String lastName = tokenizer.nextToken();
                                String dateString = tokenizer.nextToken();
                                Date date = Date.fromDateStr(dateString);

                                if (!date.isValid()) {
                                        showAlert("DOB invalid: " + dateString + " not a valid calendar date!");
                                        break;
                                }
                                if (!date.isFutureDate()) {
                                        showAlert("DOB invalid: " + dateString + " cannot be today or a future day.");
                                        break;
                                }
                                int age = date.calculateAge();

                                Account account = null;
                                boolean invalidcode = false;
                                switch (accountType) {
                                        case "C":
                                                if (age < 16) {
                                                        showAlert("DOB invalid: " + dateString + " under 16.");
                                                        break;
                                                }
                                                try {
                                                        double deposit = Double.parseDouble(tokenizer.nextToken());
                                                        if (deposit <= 0) {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                                break;
                                                        }
                                                        Profile profile = new Profile(firstName, lastName, date);
                                                        account = new Checking(profile, deposit);
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                                break;

                                        case "MM":
                                                if (age < 16) {
                                                        showAlert("DOB invalid: " + dateString + " under 16.");
                                                        break;
                                                }
                                                try {
                                                        double deposit = Double.parseDouble(tokenizer.nextToken());
                                                        if (deposit <= 0) {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                                break;
                                                        }
                                                        if (deposit < 2000) {
                                                                showAlert("Minimum of $2000 to open a Money Market account.");
                                                                break;
                                                        }
                                                        Profile profile = new Profile(firstName, lastName, date);
                                                        int withdrawal = 0;
                                                        account = new MoneyMarket(profile, deposit, true, withdrawal);
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                                break;

                                        case "S":
                                                if (age < 16) {
                                                        showAlert("DOB invalid: " + dateString + " under 16.");
                                                        break;
                                                }
                                                try {
                                                        double deposit = Double.parseDouble(tokenizer.nextToken());
                                                        int code = Integer.parseInt(tokenizer.nextToken());
                                                        boolean isLoyal = code == 1;
                                                        if (deposit <= 0) {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                                break;
                                                        }
                                                        Profile profile = new Profile(firstName, lastName, date);
                                                        account = new Savings(profile, deposit, isLoyal);

                                                        boolean contains = accountDatabase.contains(account);

                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                                break;

                                        case "CC":
                                                if (age < 16) {
                                                        showAlert("DOB invalid: " + dateString + " under 16.");
                                                        break;
                                                }
                                                if (age >= 24) {
                                                        showAlert("DOB invalid: " + dateString + " over 24.");
                                                        break;
                                                }
                                                try {
                                                        double deposit = Double.parseDouble(tokenizer.nextToken());
                                                        int code = Integer.parseInt(tokenizer.nextToken());
                                                        Campus campus = null;
                                                        switch (code) {
                                                                case 0:
                                                                        campus = com.example.rubank_ui.Campus.NEW_BRUNSWICK;
                                                                        break;
                                                                case 1:
                                                                        campus = com.example.rubank_ui.Campus.NEWARK;
                                                                        break;
                                                                case 2:
                                                                        campus = com.example.rubank_ui.Campus.CAMDEN;
                                                                        break;
                                                                default:
                                                                        showAlert("Invalid campus code.");
                                                                        invalidcode = true;
                                                                        break;
                                                        }
                                                        if (deposit <= 0) {
                                                                showAlert("Initial deposit cannot be 0 or negative.");
                                                                break;
                                                        }
                                                        Profile profile = new Profile(firstName, lastName, date);
                                                        account = new CollegeChecking(profile, deposit, campus);
                                                } catch (NumberFormatException e) {
                                                        showAlert("Not a valid amount.");
                                                }
                                                break;
                                }
                                if (invalidcode) {
                                        break;
                                }

                                if (account != null) {
                                        accountDatabase.open(account);
                                }
                        }
                        outputTextArea.appendText("Accounts loaded.");
                }
        }
}
