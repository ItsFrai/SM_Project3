package com.example.rubank_ui;
/**
 * This class represents a Checking account, which is a type of Account.
 * It includes specific methods and constants related to Checking accounts.
 *
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public class Checking extends Account {

    private static final double INTEREST_RATE = 0.01;
    private static final double MONTHLY_FEE_THRESHOLD = 1000.00;

    /**
     * A getter used to get the account_type when comparing accounts for the equals method
     */
    @Override
    public String accountType() {
        return "Checking";
    }
    @Override
    public String short_AccountType() {
        return "C";
    }

    /**
     * Constructs a Checking account with the given profile and initial deposit.
     *
     * @param profile The profile of the account holder.
     * @param deposit The initial deposit amount.
     */
    public Checking(Profile profile, double deposit) {
        super(profile, deposit);
    }

    /**
     *
     * Calculate and return the monthly interest for a Checking account.
     */
    @Override
    public double monthlyInterest() {

        return (balance * INTEREST_RATE) / 12.0;
    }

    /**
     *
     * Calculate and return the monthly fee for a Checking account.
     */
    @Override
    public double monthlyFee() {
        if (balance >= MONTHLY_FEE_THRESHOLD) {
            return 0.00;
        } else {
            return 12.00;
        }
    }

}
