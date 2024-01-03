package com.example.bank_of_patel.bank.personaldata;
import java.util.Calendar;
/**
 * Done, need to include JUNIT testing
 * Represents a specific instant of a given date.
 * Imported from Project 1
 * @author Dharmik Patel and Krish Patel
 */
public class Date implements Comparable<Date> {
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final Date PRESENT_DATE = Date.getThePresentDay();
    private final int year;
    private final Month month;
    private final int day;

    /**
     * Allocates a Date object and initializes it so that it represents
     * the day specified by the given String.
     * @param date Takes in a String date: MONTH/DAY/YEAR;Example: 09/06/2023
     */
    public Date(String date) {
        String[] tokens = date.split("/");
        this.year = Integer.parseInt(tokens[2]);
        int monthNumber = Integer.parseInt(tokens[0]);
        if (monthNumber <= Month.MAX_NUM_OF_MONTH && monthNumber >=
                Month.MIN_NUM_OF_MONTH) {
            this.month = Month.values()[monthNumber - 1];
        } else {
            this.month = Month.NON_A_MONTH;
        }
        this.day = Integer.parseInt(tokens[1]);
    }

    /**
     * Getter method to get year field.
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Getter method to get month field.
     * @return the month
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Getter method to get day field.
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * Used to test if a given date is valid. Handles leap years as well.
     * See test case methods for more specifications.
     * @return True if the date is a valid calendar date. False if it is not.
     */
    public boolean isValid() {
        if (month.equals(Month.FEBRUARY)) {
            if (isLeapYear()) {
                return day >= Month.MIN_NUM_OF_DAYS &&
                        day <= Month.DAYS_IN_FEB_LEAP;
            } else {
                return day >= Month.MIN_NUM_OF_DAYS &&
                        day <= Month.DAYS_IN_FEB_NON_LEAP;
            }
        } else if (month.equals(Month.NON_A_MONTH)) {
            return false;
        } else {
            return day >= Month.MIN_NUM_OF_DAYS &&
                    day <= month.getMaxAmountOfDays();
        }
    }

    /**
     * Used to test if given date occurs before the PRESENT_DATE.
     * @return True if given date occurs before PRESENT_DATE.
     * False if given date occurs on or after PRESENT_DATE.
     */
    public boolean isLessThanPresentDate() {
        return this.compareTo(PRESENT_DATE) <= 0;
    }

    /**
     * Utility method to set the PRESENT_DATE
     * @return Today's Date
     */
    private static Date getThePresentDay(){
        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DATE);
        int todayMonth = today.get(Calendar.MONTH)+1;
        int todayYear = today.get(Calendar.YEAR);
        return new Date(String.format("%d/%d/%d", todayMonth, todayDay,
                todayYear));
    }

    /**
     * Checks if the date occurs in a leap year.
     * @return True if year is a leap year. False if year is not a leap year.
     */
    private boolean isLeapYear() {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                if (year % QUATERCENTENNIAL == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Checks if two dates are equal.
     * @return Returns true if the day, month, and year are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date d2 = (Date) obj;
            return this.day == d2.day && this.month == d2.month &&
                    this.year == d2.year;
        }
        return false;
    }

    /**
     * Overrides the default toString method.
     * @return Returns a string in the format "MONTH/DAY/YEAR"
     */
    @Override
    public String toString() {
        return String.format("%d/%d/%d",
                month.ordinal() + 1, day, year);
    }

    /**
     * Compares 2 dates. Let this instance be "date1".
     * @param date2 the object to be compared.
     * @return Returns -1 if date1 occurs before date2.
     * Returns 0 if date1 is same as date2.
     * Returns +1 if date 1 occurs after date2.
     */
    @Override
    public int compareTo(Date date2) {
        if (this.year > date2.year) {
            return 1;
        } else if (this.year < date2.year) {
            return -1;
        } else {
            if (!(this.month.equals(date2.month))) {
                return this.month.compareTo(date2.month);
            } else {
                return Integer.compare(this.day, date2.day);
            }
        }
    }

}
