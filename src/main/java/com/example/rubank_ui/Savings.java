package com.example.rubank_ui;

/**
 * This class represents a Savings Account, a specific type of account with monthly interest and fees.
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public class Savings extends Account {
    protected boolean isLoyal;

    /**
     * Constructor for creating a Savings Account with a specified profile, initial deposit, and loyalty status.
     *
     * @param profile the account holder's profile
     * @param deposit the initial deposit amount
     * @param loyal   true if the account holder is loyal, false otherwise
     */
    public Savings(Profile profile, double deposit, boolean loyal) {
        super(profile, deposit);
        this.isLoyal = loyal;
    }

    /**
     * A getter used to get the account_type when comparing accounts for the equals method
     */
    @Override
    public String accountType() {
        return "Savings";
    }


    /**
     *
     * Gets the monthly interest rate
     */
    @Override
    public double monthlyInterest() {
        if (isLoyal){
            return (balance * 0.0425) / 12.0; // Calculate monthly interest for loyal account holders
        } else {
            return (balance * 0.040) / 12.0;  // Calculate monthly interest for non-loyal account holders
        }

    }

    /**
     *
     * Gets the monthly fee for each account
     */
    @Override
    public double monthlyFee() {
        if (balance >= 500.0) {
            return 0;// No monthly fee if the balance is $500 or more
        } else {
            return 25.0; // Monthly fee for balances below $500
        }
    }
    /**
     *
     * Convert Savings account to string to print
     */
    @Override
    public String toString() {
        return super.toString() + (isLoyal ? "::is loyal" : "");
    }// Include loyalty status in the account's string representation

    /**
     *
     * Gets the short version of accountType string
     */
    public String short_AccountType() {
        return "S";
    }
}
