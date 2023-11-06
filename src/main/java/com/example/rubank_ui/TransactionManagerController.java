package com.example.rubank_ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Controller for handling account opening and related operations to a banking transaction.
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public class TransactionManagerController {

        public TextArea mainOutput;
        public TextArea deposit_output;
        public TextArea outputTextArea;
        public TextField firstname_for_deposit;
        public TextField last_name_for_deposit;
        public DatePicker DOBLabel_for_deposit;
        public TextField amount_for_deposit;

        @FXML
        private ToggleGroup Account;

        @FXML
        private ToggleGroup AccountDeposit;

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

        /**
         * Handle the selection of the College Checking account type.
         * This method disables the loyalCustomerCheckbox, and enables the Campus radio buttons.
         */
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



        /**
         * Handle the selection of account types other than College Checking.
         * This method checks the selected account type, enables or disables the loyalCustomerCheckbox,
         * and modifies the Campus radio buttons accordingly.
         */
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

        /**
         * Clear all input fields and selections on the form.
         * This method resets the values of first name, last name, date of birth, amount, and radio button selections.
         */
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

        /**
         * Display an error alert dialog with the given message.
         *
         * @param message The error message to be displayed in the alert.
         */
        @FXML
        private void showAlert(String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
        }

        /**
         * Attempt to open a new account based on user input and validate the provided data.
         * This method performs a series of checks and validations, including account type, personal information,
         * and deposit amount, to open a new account.
         */
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
                        }
                }
                String[] dateString;
                Date date = null;

                String newStringDate = null;

                if (DOBLabel.getValue() != null) {
                        dateString = DOBLabel.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).split("/");

                        if (dateString.length == 3) {
                                int month = Integer.parseInt(dateString[1]);
                                int day = Integer.parseInt(dateString[2]);
                                int year = Integer.parseInt(dateString[0]);
                                date = new Date(year,month, day);
                                newStringDate = (month + "/" + day + "/" + year);
                        }
                } else {
                        showAlert("Missing data for opening an account.");
                }

                if (date != null && date.isValid() && date.isFutureDate() && selectedAccountType != null) {
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
                                                showAlert("DOB invalid: " + newStringDate + " under 16.");
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
                                                showAlert("DOB invalid: " + newStringDate + " under 16.");
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
                                                showAlert("DOB invalid: " + newStringDate + " under 16.");
                                        }
                                        break;

                                case "College Checking":
                                        if (age >= 16 && age <= 24) {
                                                try {
                                                        double deposit = Double.parseDouble(amount.getText());
                                                        RadioButton selectedRadioButtonforCampus = (RadioButton) Campus.getSelectedToggle();
                                                        String campusName;
                                                        if (selectedRadioButtonforCampus != null) {
                                                                campusName = selectedRadioButtonforCampus.getText();
                                                        } else {
                                                                showAlert("Missing data for opening an account.");
                                                                break;
                                                        }

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
                                                        showAlert("DOB invalid: " + newStringDate + " under 16.");
                                                } else {
                                                        showAlert("DOB invalid: " + newStringDate + " over 24.");
                                                }
                                        }
                                        break;

                                default:
                                        showAlert("Invalid account type.");
                        }

                        if (account != null) {
                                if (accountDatabase.open(account)) {
                                        mainOutput.appendText(firstName + " " + lastName + " " + newStringDate + " (" + account.short_AccountType() + ")" + " opened.\n");
                                } else {
                                        showAlert(firstName + " " + lastName + " " + newStringDate + " (" + account.short_AccountType() + ")" + " is already in the database\n");
                                }
                        }
                } else {
                        if (date != null) {
                                showAlert("DOB invalid:" + newStringDate +  " not a valid calendar date or cannot be today or a future day.");
                        }
                }
        }

        /**
         * Attempt to close an account based on user input and validate the provided data.
         * This method checks the selected account type, personal information, and date of birth to close an account.
         */
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
                        }

                }
                // Parse and validate the date of birth.
                String[] dateString;
                Date date = null;

                String newStringDate = null;

                // Validate the date, account type, and close the account if possible.
                if (DOBLabel.getValue() != null) {
                        dateString = DOBLabel.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).split("/");

                        if (dateString.length == 3) {
                                int month = Integer.parseInt(dateString[1]);
                                int day = Integer.parseInt(dateString[2]);
                                int year = Integer.parseInt(dateString[0]);
                                date = new Date(year,month, day);
                                newStringDate = (month + "/" + day + "/" + year);
                        }
                } else {
                        showAlert("Missing data for opening an account.");
                }
                if (date != null && date.isFutureDate()  && selectedAccountType != null) {

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
                                        mainOutput.appendText(firstName + " " + lastName + " " + newStringDate + " (" + accountToClose.short_AccountType() + ")" + " has been closed.\n");
                                } else {
                                        if (!firstName.isEmpty() && !lastName.isEmpty()) {
                                                showAlert(firstName + " " + lastName + " " + newStringDate + " (" + accountToClose.short_AccountType() + ")" + " is not in the database\n");
                                        }
                                }
                        } else {
                                showAlert("Invalid account type: " + selectedAccountType);
                        }
                } else {
                        if (date != null) {
                                showAlert("DOB invalid:" + newStringDate + " not a valid calendar date or cannot be today or a future day.");
                        }
                }
        }

        /**
         * Attempt to deposit funds into an account based on user input and validate the provided data.
         * This method checks the selected account type, personal information, and deposit amount to make a deposit.
         */
        @FXML
        protected void depositAccount() {
                boolean errorEncountered = false;

                // Get the selected account type for deposit.
                RadioButton selectedRadioButton = (RadioButton) AccountDeposit.getSelectedToggle();
                String selectedAccountType = null;
                if (selectedRadioButton != null) {
                        selectedAccountType = selectedRadioButton.getText();
                } else {
                        showAlert("Missing data for opening an account.");
                        errorEncountered = true;
                }

                // Get the first name for the deposit.
                String firstName = firstname_for_deposit.getText();

                if (firstName == null || firstName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String lastName = last_name_for_deposit.getText();
                if (lastName == null || lastName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                        }
                }
                String[] dateString;
                Date date = null;

                String newStringDate = null;

                if (DOBLabel.getValue() != null) {
                        dateString = DOBLabel.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).split("/");

                        if (dateString.length == 3) {
                                int month = Integer.parseInt(dateString[1]);
                                int day = Integer.parseInt(dateString[2]);
                                int year = Integer.parseInt(dateString[0]);
                                date = new Date(year,month, day);
                                newStringDate = (month + "/" + day + "/" + year);
                        }
                } else {
                        showAlert("Missing data for opening an account.");
                }
                if (date != null && date.isFutureDate()  && selectedAccountType != null && date.isValid()) {
                        double depositAmount;
                        try {
                                depositAmount = Double.parseDouble(amount_for_deposit.getText());
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
                        if (firstName != null && lastName != null) {
                                String depositMessage = accountDatabase.deposit(shellAccount);
                                        if (depositMessage.contains("not in the database")) {
                                                showAlert(depositMessage);
                                        } else {
                                                deposit_output.appendText(depositMessage);
                                }
                        } else {
                                if (date != null) {
                                        showAlert("DOB invalid:" + newStringDate +  " not a valid calendar date or cannot be today or a future day.");
                                }
                        }
                }
        }


        /**
         * Attempt to withdraw funds from an account based on user input and validate the provided data.
         * This method checks the selected account type, personal information, withdrawal amount, and attempts a withdrawal.
         */
        @FXML
        protected void withdrawalAccount() {
                boolean errorEncountered = false;

                RadioButton selectedRadioButton = (RadioButton) AccountDeposit.getSelectedToggle();

                String selectedAccountType = null;
                if (selectedRadioButton != null) {
                        selectedAccountType = selectedRadioButton.getText();
                } else {
                        showAlert("Missing data for opening an account.");
                        errorEncountered = true;
                }

                String firstName = firstname_for_deposit.getText();

                if (firstName == null || firstName.isEmpty()  && selectedAccountType != null) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                                errorEncountered = true;
                        }
                }
                String lastName = last_name_for_deposit.getText();
                if (lastName == null || lastName.isEmpty()) {
                        if (!errorEncountered) {
                                showAlert("Missing data for opening an account.");
                        }
                }
                String[] dateString;
                Date date = null;

                String newStringDate = null;

                if (DOBLabel.getValue() != null) {
                        dateString = DOBLabel.getValue().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")).split("/");

                        if (dateString.length == 3) {
                                int month = Integer.parseInt(dateString[1]);
                                int day = Integer.parseInt(dateString[2]);
                                int year = Integer.parseInt(dateString[0]);
                                date = new Date(year,month, day);
                                newStringDate = (month + "/" + day + "/" + year);
                        }
                } else {
                        showAlert("Missing data for opening an account.");
                }
                // Validate the date, account type, and perform the withdrawal if possible.
                if (date != null && date.isFutureDate() && selectedAccountType != null && date.isValid()) {
                        double withdrawalAmount;
                        try {
                                withdrawalAmount = Double.parseDouble(amount_for_deposit.getText());
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
                                                deposit_output.appendText(firstName + " " + lastName + " " + newStringDate + " (" + shellAccount.short_AccountType() + ") Withdrawal - balance updated.\n");
                                        } else {
                                                showAlert(firstName + " " + lastName + " " + newStringDate + " (" + shellAccount.short_AccountType() + ") Withdrawal - insufficient funds.");
                                        }
                                }
                        }

                        if (!accountExists) {
                                showAlert(firstName + " " + lastName + " " + newStringDate + " (" + shellAccount.short_AccountType() + ") is not in the database.");
                        }
                } else {
                        if (date != null) {
                                showAlert("DOB invalid:" + newStringDate +  " not a valid calendar date or cannot be today or a future day.");
                        }
                }
        }

        /**
         * Print a sorted list of accounts and display them in the output text area.
         */
        @FXML
        protected void printSortedAccounts() {
                String sortedAccounts = accountDatabase.printSorted();
                outputTextArea.appendText(sortedAccounts);
        }

        /**
         * Print a list of fees and interests in the accounts and display them in the output text area.
         */
        @FXML
        protected void printFeesAndInterests() {
                String output = accountDatabase.printFeesAndInterests();
                outputTextArea.appendText(output);
        }

        /**
         * Print and display the updated balances for all accounts in the output text area.
         */
        @FXML
        protected void printUpdatedBalances() {
                String output = accountDatabase.printUpdatedBalances();
                outputTextArea.appendText(output);
        }

        /**
         * Load account data from a file, validate the data, and create new accounts accordingly.
         * The method reads the data line by line and processes it to open accounts based on the provided information.
         *
         * @throws FileNotFoundException if the specified file is not found.
         */
        @FXML
        protected void load_accounts() throws FileNotFoundException {
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(new Stage());

                if (selectedFile != null) {
                        Scanner scanner = new Scanner(selectedFile);
                        while (scanner.hasNextLine()) {

                                String line = scanner.nextLine();

                                if (line.trim().isEmpty()) {
                                        continue;
                                }
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
                        outputTextArea.appendText("Accounts loaded.\n");
                }
        }
}
