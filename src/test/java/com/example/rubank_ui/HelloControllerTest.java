package com.example.rubank_ui;

import javafx.scene.control.RadioButton;
import org.junit.jupiter.api.Test;




import static org.junit.jupiter.api.Assertions.*;

class HelloControllerTest {

    @Test
    void ClosedAccountNotFound(){
        AccountDatabase accountDatabase = new AccountDatabase();

        String firstName = "NineO";
        String lastName = "Shooter";
        String dob = "12/5/2005";
        Date newDob = Date.fromDateStr(dob);

        Profile profile = new Profile(firstName,lastName,newDob);

        Account account = new Savings(profile, 1000, true);

        accountDatabase.open(account);

        String fname = "Nine";
        String lname = "Shooter";
        String dob1 = "12/5/2005";
        Date newDob1 = Date.fromDateStr(dob1);
        Profile profile1 = new Profile(fname,lname,newDob1);

        Account nonExistent_Acc = new Savings(profile1, 1000, true);
        boolean result = accountDatabase.close(nonExistent_Acc);

        assertFalse(result);

    }

    @Test
    void AccountOpenedFound(){
        AccountDatabase accountDatabase = new AccountDatabase();

        String firstName = "NineO";
        String lastName = "Shooter";
        String dob = "12/5/2005";
        Date newDob = Date.fromDateStr(dob);

        Profile profile = new Profile(firstName,lastName,newDob);

        Account account = new Savings(profile, 1000, true);

        accountDatabase.open(account);



        boolean result = accountDatabase.close(account);

        assertTrue(result);

    }



}