/*
 *  @(#)Interval.java	1.0 2009/08/06
 *
 */
package jmortgage.payment;

import java.util.Calendar;

/**
 * Enum that represents the amount of time between payments. This enum is used
 * when building an implementation of {@link PmtCalculator} to calcluate the
 * interval payment for a mortgage term. It is used when building an
 * implementation of {@link jmortgage.amortization.PmtKey} to determine how far
 * to increment subsequent payment keys.
 * @author David Armstrong
 * @version 1.0
 */
public enum Interval {

    /**
     * Weekly interval
     */
    Weekly(Calendar.WEEK_OF_YEAR, 1, 52),

    /**
     * Biweekly interval
     */
    Biweekly(Calendar.WEEK_OF_YEAR, 2, 26),

    /**
     * Monthly interval
     */
    Monthly(Calendar.MONTH, 1, 12),

    /**
     * Yearly interval. This is valid only as a value for extra payment
     * intervals.
     */
    Yearly(Calendar.YEAR, 1, 1),

    /**
     * Onetime interval. This is valid only as a value for extra payment
     * intervals.
     */
    Onetime(Calendar.YEAR, 0, 0); // Set duration to Calendar.YEAR just so that it will be a valid value from perspective of Calendar. The increment value of 0 means it will not increment.

    // The duration is a value that is used in the Calendar.add method to determine the next key value.
    private final int duration;
    // The increment is a value that is used in the Calendar.add method to determine the next key value. The increment value is the value needed to increment the key value by 1.
    private final int increment;
    // The pmtsPerYear is the number of payments per year for the interval.
    private final int pmtsPerYear;

    Interval(int duration, int increment, int pmtsPerYear) {
        this.duration = duration;
        this.increment = increment;
        this.pmtsPerYear = pmtsPerYear;
    }

    /**
     * Gets the duration to be used when determining the next key value in a
     * {@link jmortgage.amortization.PmtKey} instance.
     * @return <tt>int</tt> duration value
     */
    public int duration() { return duration; }

    /**
     * Gets the increment value to be used when determining the next key value
     * in a <tt>PmtKey</tt> instance.
     * @return <tt>int</tt> increment value
     */
    public int increment() { return increment; }

    /**
     * Gets the number of payments per year for an interval.
     * @return <tt>int</tt> payments per year value
     */
    public int pmtsPerYear() { return pmtsPerYear; }

}
