package com.example.rubank_ui;

import java.text.DecimalFormat;

/**
 * Represents a Money Market account that extends the Savings class.
 *
 * This class provides methods to manage a Money Market account, including calculating
 * withdrawal fees, monthly interest, and handling loyalty status.
 *
 * @author @author Fraidoon Pourooshasb, Samman Pandey
 */
public class MoneyMarket extends Savings {
    private int withdrawl; //number of withdrawals


    /**
     * Constructs a MoneyMarket account with the specified profile, initial deposit, loyalty status, and number of withdrawals.
     *
     * @param profile    The profile associated with the account.
     * @param deposit    The initial deposit for the account.
     * @param isLoyal    Indicates whether the account holder is eligible for loyalty status.
     * @param withdrawal The number of withdrawals made from the account.
     */
    public MoneyMarket(Profile profile, double deposit, boolean isLoyal, int withdrawal) {
        super(profile, deposit, isLoyal);
        this.withdrawl = withdrawal;
    }

    /**
     * A getter used to get the account_type when comparing accounts for the equals method
     */
    @Override
    public String accountType() {
        return "MoneyMarket";
    }

    /**
     *
     * Gets the short string version for printing
     */
    @Override
    public String short_AccountType() {
        return "MM";
    }


    /**
     *
     * Gets the withdrawal fee for the account
     */
    @Override
    public double withdrawalFee() {
        if (withdrawl > 3.0) {
            return 10.0;
        }

        return 0;
    }
    /**
     * Checks if the account holder is eligible for loyalty status.
     *
     * @return true if the balance is greater than or equal to 2000.0, indicating loyalty status.
     */

    public boolean loyaltyStatus(){
        return (balance >= 2000.0);
    }

    /**
     * Calculates the monthly fee for the Money Market account based on the account balance.
     *
     * If the account balance is greater than or equal to 2000.0, no monthly fee is applied.
     * If the balance is below 2000.0, a monthly fee of $25.00 is applied.
     *
     * @return The calculated monthly fee as a double value.
     */
    @Override
    public double monthlyFee() {
        if (balance>=2000.0){
            return 0;
        }
        else{
            return 25.00;
        }
    }

    /**
     * Returns a formatted string representation of the Money Market account.
     *
     * @return A formatted string representing the Money Market account.
     */
    @Override
    public String toString() {
        if (!loyaltyStatus()) {
            isLoyal = false;
        }
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return accountType() + "::Savings::" + holder + "::Balance $" + df.format(balance) + "::is loyal" + "::withdrawal: " + withdrawl ;
    }

    /**
     * Calculates the monthly interest for the Money Market account based on loyalty status.
     *
     * @return The calculated monthly interest as a double value.
     */
    @Override
    public double monthlyInterest() {
        if (loyaltyStatus()){
            return (balance*0.0475)/12.0;

        }
        else{
            return (balance*0.045)/12.0;
        }
    }
    /**
     * Increases the withdrawal count by 1.
     */
    public void increaseWithdrawal() {
        withdrawl ++;
    }

    /**
     * Resets the withdrawal count to 0.
     */
    public void resetWithdrawal() {
        withdrawl = 0;
    }
}
