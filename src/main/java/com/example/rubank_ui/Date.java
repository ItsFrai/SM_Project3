
package com.example.rubank_ui;

import java.util.Calendar;


/**
 * Represents a date with year, month, and day attributes.
 * This class provides methods to work with dates, including validation,
 * checking if the date is in the future, and comparing dates.
 * @author Fraidoon Pourooshasb, Samman Pandey
 */
public class Date implements Comparable<Date> {

    private final int year;
    private final int month;
    private final int day;

    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Constructs a new Date object with the specified year, month, and day.
     * @return the year of the date
     */
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /**
     * Parses a date from a string in the format "month/day/year".
     *
     * @param dateString The date string to parse.
     * @return A Date object if parsing, returns null if not successful
     */
    public static Date fromDateStr(String dateString) {
        String[] parts = dateString.split("/");
        if (parts.length == 3) {
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return new Date(year, month, day);
        }
        return null;
    }

    /**
     * Checks if the date is a valid date (month between 1 and 12, day within the valid range for the month).
     *
     * @return True if the date is valid, false otherwise.
     */
    public boolean isValid() {
        // Check if the date is valid (month between 1 and 12, day within the valid range for the month)
        return month >= 1 && month <= 12 && day >= 1 && day <= getDaysInMonth();
    }

    /**
     * Checks if the date is in the future compared to the current date.
     *
     * @return True if the date is in the future, false otherwise.
     */
    public boolean isFutureDate() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        return year < currentYear || (year == currentYear && month < currentMonth)
                || (year == currentYear && month == currentMonth && day < currentDay);
    }

    /**
     * Checks if the date is more than 6 months away from the current date.
     *
     * @return True if the date is more than 6 months away, false otherwise.
     */
    public boolean isMoreThan6MonthsAway() {// Get the current date
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int futureYear = currentYear;
        int futureMonth = currentMonth + 6;
        if (futureMonth > 12) {
            futureYear += 1;
            futureMonth -= 12;
        }
        return year > futureYear || (year == futureYear && month > futureMonth || (year == futureYear && month == futureMonth && currentDay < day));
    }

    /**
     * This method gets and calculates the number of days in the current month
     * based on the month and checks for a leap year condition for February.
     *
     * @return The number of days in the current month.
     */
    private int getDaysInMonth() {
        int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear() && month == 2) {
            return 29; // February in a leap year
        }
        return daysInMonth[month];
    }

    /**

     * using the leap year rule (divisible by 4 and not divisible by 100,
     * or divisible by 400) check if it is a leap year or not.
     *
     * @return True if the current year is a leap year, false otherwise.
     */
    private boolean isLeapYear() {
        return (year % QUADRENNIAL == 0 && year % CENTENNIAL != 0) || (year % QUATERCENTENNIAL == 0);
    }

    public int calculateAge() {
        Calendar currentDate = Calendar.getInstance();
        int currentYear = currentDate.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - year;

        // Adjust the age if the birth date hasn't occurred yet this year
        if (currentMonth < month || (currentMonth == month && currentDay < day)) {
            age--;
        }

        return age;
    }

    /**
     * Compares the current date to another date
     *
     * @param date The date to compare to.
     * @return A positive integer if this date is after x, a negative integer if before x,
     *         or zero if they are equal.
     */
    @Override
    public int compareTo(Date date) {
        if (this.year > date.year) {
            return 1;
        } else if (this.year < date.year) {
            return -1;
        } else { // Years are equal, so compare months
            if (this.month > date.month) {
                return 1;
            } else if (this.month < date.month) {
                return -1;
            } else { // Months are equal, so compare days
                if (this.day > date.day) {
                    return 1;
                } else if (this.day < date.day) {
                    return -1;
                } else { // Years, months, and days are equal
                    return 0;
                }
            }
        }
    }


    /** Where the test runs */
    public static void main(String[] args) {
        testDayinFeb_nonleap();
        testDayinFeb_leap();
        testMonth();
    }

    /** Test case 1 */
    private static void testDayinFeb_nonleap() {
        Date date = new Date(2023, 2, 29);
        boolean expectedOutput = false;
        boolean actualOutput = date.isValid();
        System.out.println("**Test case #1: # of days in Feb. in a non-leap year is 28");
        testingresults(date,expectedOutput,actualOutput);
    }

    /** Test case 2 */
    private static void testDayinFeb_leap() {
        Date date = new Date(2020, 2, 30);
        boolean expectedOutput = false;
        boolean actualOutput = date.isValid();
        System.out.println("**Test case #2: # of days in Feb. in a leap year is 29");
        testingresults(date,expectedOutput,actualOutput);
    }

    /** Test case 3 */
    private static void testMonth(){
        Date date = new Date(2023, 14, 25);
        boolean expectedOutput = false;
        boolean actualOutput = date.isValid();
        System.out.println("**Test case #3: Month value should be between 1 and 12");
        testingresults(date,expectedOutput,actualOutput);
    }

    /**
     * Prints the input date, expected output, and actual output for a test case.
     * Additionally, it displays whether the test passed or failed based on the comparison
     * of the expected and actual results.
     *
     * @param date     The input date for the test case.
     * @param expected The expected output for the test case.
     * @param actual   The actual output for the test case.
     */
    private static void testingresults(Date date, boolean expected, boolean actual) {
        System.out.println("Date Input: " + date);
        System.out.println("Expected Output: " + expected);
        System.out.println("Actual Output: " + actual);

        if (expected == actual) {
            System.out.println("Test Result: Passed\n");
        } else {
            System.out.println("Test Result: Failed\n");
        }
    }
}

