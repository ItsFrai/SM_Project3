package com.example.rubank_ui;

import java.text.DecimalFormat;



/**
 * This abstract class represents an Account and provides common functionality for different account types.
 *
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public abstract class Account implements Comparable<Account> {
    protected Profile holder;
    protected double balance;


    /**
     * Abstract method to get the account type (to be implemented by subclasses).
     *
     * @return the account type
     */
    public abstract String accountType();


    /**
     * Abstract method to get an AA code (to be implemented by subclasses).
     *
     * @return the AA code
     */

    public abstract String short_AccountType();

    /**
     * Constructor for creating an Account with a given profile and initial deposit.
     *
     * @param profile  the account holder's profile
     * @param deposit  the initial deposit amount
     */
    public Account(Profile profile, double deposit) {
        this.holder = profile;
        this.balance = deposit;
    }

    /**
     * Get the current balance of the account.
     *
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Checks if the accounts are equal
     *
     * @param obj takes in any object
     * @return true if the objects are equal, and false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        Account account = (Account) obj;
        if (!holder.equals(account.holder)) {
            return false;
        }
        if (this.accountType().equals("Checking") && account.accountType().equals("CollegeChecking") && account.balance == 0.0) {
            return false;
        }
        if (this.accountType().equals("CollegeChecking") && account.accountType().equals("Checking") && account.balance == 0.0) {
            return false;
        }
        if (this.accountType().equals("Checking") && account.accountType().equals("CollegeChecking")) {
            return true;
        }
        if (this.accountType().equals("CollegeChecking") && account.accountType().equals("Checking")) {
            return true;
        }


        return getClass() == obj.getClass();
    }

    /**
     * Check if this Account is equal to another Account with an additional integer parameter.
     * This method considers account types only.
     *
     * @param obj  the Account to compare to
     * @param x    an additional parameter to account for the withdrawl amount(overloading)
     * @return true if the accounts are of the same type, false otherwise
     */
    public boolean equals(Object obj, int x) {
        Account account = (Account) obj;
        if (!holder.equals(account.holder)) {
            return false;
        }
        if (this.accountType().equals("Checking") && account.accountType().equals("CollegeChecking")) {
            return false;
        }
        if (this.accountType().equals("CollegeChecking") && account.accountType().equals("Checking")) {
            return false;
        }


        return getClass() == obj.getClass();
    }



    /**
     * Compares this account to another account for sorting purposes based on their date and start time.
     *
     * @param account,  The account to compare to this account.
     * @return 0 if both accounts are equal, a positive value if this account is greater,
     * and a negative value if this account is lesser.
     */
    @Override
    public int compareTo(Account account) {
        int accountTypeComparison = this.accountType().compareTo(account.accountType());
        if (accountTypeComparison != 0) {
            return accountTypeComparison;
        }

        return this.holder.compareTo(account.holder);
    }

    /**
     * Returns a string representation of this account transaction,
     * including its date of birth, the names and the balance in the account.
     *
     * @return A formatted string representing the account.
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return accountType() + "::" + holder + "::Balance $" + df.format(balance);
    }


    /**
     * Get the monthly interest for the account (abstract method to be implemented by subclasses).
     *
     * @return the monthly interest
     */
    public abstract double monthlyInterest();

    /**
     * Get the monthly fee for the account (abstract method to be implemented by subclasses).
     *
     * @return the monthly fee
     */
    public abstract double monthlyFee();


    /**
     * Deposit a specified amount into the account, if the amount is positive.
     *
     * @param amount  the amount to deposit
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    /**
     * Withdraw a specified amount from the account, if the balance is sufficient.
     *
     * @param amount  the amount to withdraw
     * @return true if the withdrawal is successful, false otherwise
     */
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    /**
     * Get the withdrawal fee for the account (default implementation returns 0).
     *
     * @return the withdrawal fee
     */
    public double withdrawalFee(){
        return 0;
    }


    /**
     * Get the account holder's profile.
     *
     * @return the account holder's profile
     */
    public Profile getHolder() {
        return holder;
    }

}
