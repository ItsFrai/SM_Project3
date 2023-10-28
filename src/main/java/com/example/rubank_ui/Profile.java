package com.example.rubank_ui;

/**
 * This class represents a profile, which includes a first name, last name, and date of birth.
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private final Date dob;


    /**
     * Constructor for creating a profile with a first name, last name, and date of birth.
     *
     * @param firstName   the first name
     * @param lastName    the last name
     * @param dateOfBirth the date of birth
     */
    public Profile(String firstName, String lastName, Date dateOfBirth) {
        this.fname = firstName;
        this.lname = lastName;
        this.dob = dateOfBirth;
    }

    /**
     * Compares this profile to another profile based on last name, first name, and date of birth.
     *
     * @param other the profile to compare to
     * @return a negative value if this profile is less than the other, 0 if they are equal,
     * or a positive value if this profile is greater than the other
     */
    @Override
    public int compareTo(Profile other) { // Comparison based on last name, first name, and date of birth
        int lastNameComparison = this.lname.compareTo(other.lname);
        if (lastNameComparison != 0) {
            return lastNameComparison;
        }

        int firstNameComparison = this.fname.compareTo(other.fname);
        if (firstNameComparison != 0) {
            return firstNameComparison;
        }
        int yearComparison = this.dob.getYear() - other.dob.getYear();
        if (yearComparison != 0) {
            return yearComparison;
        }

        int monthComparison = this.dob.getMonth() - other.dob.getMonth();
        if (monthComparison != 0) {
            return monthComparison;
        }

        return this.dob.getDay() - other.dob.getDay();
    }

    /**
     * Checks if this profile is equal to another profile based on first name, last name, and date of birth.
     *
     * @param obj the object to compare to
     * @return true if the profiles are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        Profile otherProfile = (Profile) obj;
        return fname.equalsIgnoreCase(otherProfile.fname) &&
                lname.equalsIgnoreCase(otherProfile.lname) && dob.getYear() == otherProfile.dob.getYear() &&
                dob.getMonth() == otherProfile.dob.getMonth() &&
                dob.getDay() == otherProfile.dob.getDay();
    }


    /**
     * Returns a string representation of the profile in the format: "First Name Last Name MM/DD/YYYY".
     *
     * @return the string representation of the profile
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.getMonth() + "/" + dob.getDay() + "/" + dob.getYear();
    }
}


