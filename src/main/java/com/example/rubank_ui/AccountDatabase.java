package com.example.rubank_ui;

/**
 * This class represents a database of accounts and provides various operations on those accounts.
 *  @author Fraidoon Pourooshasb, Samman Pandey
 */
public class AccountDatabase {

    /**
     * Default constructor for the AccountDatabase.
     */
    public AccountDatabase() {
        accounts = new Account[INITIAL_CAPACITY];
        numAcct = 0;
    }
    private static final int NOT_FOUND = -1;//A constant indicating that an account was not found in the database.

    private static final int INITIAL_CAPACITY = 4;//The initial capacity of the accounts array.

    private Account [] accounts; //list of various types of accounts
    private int numAcct; //number of accounts in the array

    /**
     * Find the index of an account in the accounts array.
     *
     * @param account the account to find
     * @return the index of the account, or NOT_FOUND if not found
     */
    private int find(Account account) {
        for (int i = 0; i < numAcct; i++) {
            if (accounts[i].equals(account)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Get the number of accounts in the database.
     *
     * @return the number of accounts in the database
     */
    public int getNumAcct() {
        return numAcct;
    }

    /**
     * Get the array of accounts in the database.
     *
     * @return the array of accounts
     */
    public Account[] getAccounts() {
        return accounts;
    }

    /**
     * Increase the capacity of the accounts array by 4.
     */
    private void grow(){
        Account[] new_accounts = new Account[accounts.length + 4];

        // Copy elements from the old array to the new one
        for (int i = 0; i < accounts.length; i++) {
            new_accounts[i] = accounts[i];
        }
        // Update the reference to the events array
        accounts = new_accounts;

    } //increase the capacity by 4

    /**
     * Check if the database contains a given account.
     *
     * @param account the account to check
     * @return true if the account is in the database, false otherwise
     */
    public boolean contains(Account account){
        int found = find(account);
        return found != NOT_FOUND;
    }

    /**
     * Add a new account to the database.
     *
     * @param account the account to be added
     * @return true if the account was successfully added, false otherwise
     */
    public boolean open(Account account){
        int duplicateIndex = find(account);
        if (duplicateIndex != NOT_FOUND) {
            return false;
        }
        if (numAcct == accounts.length) {
            grow();
        }
        accounts[numAcct] = account;
        numAcct++;
        return true;
    } //add a new account

    /**
     * Close an account in the database.
     *
     * @param account the account to be closed
     * @return true if the account was successfully closed, false otherwise
     */
    public boolean close(Account account){
        int found = find(account);
        if (found == NOT_FOUND) {
            return false;
        } else {
            for (int i = found; i < numAcct - 1; i++) {
                accounts[i] = accounts[i + 1];
            }
            accounts[numAcct - 1] = null;
            numAcct--;
            return true;
        }
    }

    /**
     * Withdraw funds from an account.
     *
     * @param account the account from which to withdraw funds
     * @return true if the withdrawal was successful, false otherwise
     */
    public boolean withdraw(Account account){
        for (int i = 0; i < numAcct; i++ ) {
            if (accounts[i].equals(account)){
                if (accounts[i].withdraw(account.getBalance())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Deposit funds into an account.
     *
     * @param account the account in which to deposit funds
     */
    public void deposit(Account account){
        boolean accountExists = false;
        for (int i = 0; i < numAcct; i++) {
            if (accounts[i].equals(account, 5)) {
                accountExists = true;
                accounts[i].deposit(account.getBalance());
                String message = account.getHolder().toString() + " (" + account.short_AccountType() + ") ";
                if (account.getBalance() == 0) {
                    System.out.println(message + "is not in the database.");
                } else {
                    System.out.println(message + "Deposit - balance updated.");
                }
                break;
            }
        }
        if (!accountExists) {
            System.out.println(account.getHolder().toString() + " (" + account.short_AccountType() + ") is not in the database.");
        }
    }


    /**
     * Print the accounts sorted by account type and profile.
     */
    public void printSorted() {
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            System.out.println("* Accounts sorted by account type and profile.");
            for (int i = 0; i < numAcct - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < numAcct - i - 1; j++) {
                    if (accounts[j].compareTo(accounts[j + 1]) > 0) {
                        Account temp = accounts[j];
                        accounts[j] = accounts[j + 1];
                        accounts[j + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
            for (int i = 0; i < numAcct; i++) {
                System.out.println(accounts[i].toString());
            }
            System.out.println("*end of list.");
        }
    }

    /**
     * Print a list of accounts with fees and monthly interest calculated.
     */
    public void printFeesAndInterests(){
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            System.out.println("*list of accounts with fee and monthly interest");
            for (int i = 0; i < numAcct - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < numAcct - i - 1; j++) {
                    if (accounts[j].compareTo(accounts[j + 1]) > 0) {
                        Account temp = accounts[j];
                        accounts[j] = accounts[j + 1];
                        accounts[j + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
            for (int i = 0; i < numAcct; i++) {
                double MFee = accounts[i].monthlyFee()+accounts[i].withdrawalFee();;
                double MIntrestrate=accounts[i].monthlyInterest();
                System.out.println(accounts[i].toString()+"::fee $"+String.format("%.2f", MFee)+ "::monthly interest $"+String.format("%.2f", MIntrestrate));

            }
            System.out.println("*end of list.");
        }
    } //calculate interests/fees

    /**
     * Print a list of accounts with fees and interests applied, updating the account balances.
     */
    public void printUpdatedBalances() {
        if (numAcct == 0) {
            System.out.println("Account Database is empty!");
        } else {
            System.out.println("*list of accounts with fees and interests applied.");
            for (int i = 0; i < numAcct - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < numAcct - i - 1; j++) {
                    if (accounts[j].compareTo(accounts[j + 1]) > 0) {
                        Account temp = accounts[j];
                        accounts[j] = accounts[j + 1];
                        accounts[j + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
            for (int i = 0; i < numAcct; i++) {

                double MFee = accounts[i].monthlyFee()+accounts[i].withdrawalFee();
                double MIntrestrate = accounts[i].monthlyInterest();
                accounts[i].balance= accounts[i].balance - MFee + MIntrestrate;

                if (accounts[i] instanceof MoneyMarket) {
                    ((MoneyMarket) accounts[i]).resetWithdrawal();
                }

                System.out.println(accounts[i].toString());

            }
            System.out.println("*end of list.");
        }
    }
}
