package com.example.rubank_ui;

/**
 * This enum represents different campuses with associated college codes.
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public enum Campus {
    NEW_BRUNSWICK(0), NEWARK(1),CAMDEN(2);

private final int collegeCode;

    /**
     * Constructor for the Campus enum. Associates a college code with each campus.
     *
     * @param code the college code to associate with the campus
     */
Campus(int code){
        this.collegeCode =code;
    }
public int getCollegeCode(){
        return collegeCode;
    }


}

