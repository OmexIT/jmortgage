/*
 *  @(#)DefaultPmtKey.java	1.0 2009/08/06
 *
 */
package jmortgage.amortization;

import jmortgage.payment.Interval;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The default implementation of <tt>PmtKey</tt>. This object calculates
 * the Long value of a Date or Calendar object. The Long object acts as the key
 * for Amortization schedule maps. <tt>DefaultPmtKey</tt> also encapsulates an
 * <tt>Interval</tt> object which indicates the length of time between payment
 * due dates. This object is immutable, so thread safety is guaranteed.
 * @author David Armstrong
 * @version 1.0
 */
public final class DefaultPmtKey implements PmtKey {

    private final Long key;
    private Interval interval;

    /**
     * Creates an object using the specified interval. The date defaults to
     * midnight of the current date.
     * @param interval The <tt>Interval</tt> object
     */
    public DefaultPmtKey(final Interval interval) {

        this(interval, Calendar.getInstance());
    }

    /**
     * Creates an object using the specified interval and Calendar object. The
     * Long value of the key gets converted to midnight of the specified
     * Calendar object.
     * @param interval The <tt>Interval</tt> object
     * @param cal The <tt>Calendar</tt> object
     */
    public DefaultPmtKey(final Interval interval, final Calendar cal) {

        if(interval == null) {
            throw new IllegalArgumentException("The Interval object must not be null.");
        }
        if(cal == null) {
            throw new IllegalArgumentException("The Calendar object must not be null.");
        }

        key = convertCalendarToKey(setToMidnight(cal));
        this.interval = interval;
    }

    /**
     * Creates an object using the specified interval and Date object. The Long
     * value of the key gets converted to midnight of the date specified in the
     * Date object.
     * @param interval The <tt>Interval</tt> object
     * @param date The <tt>Date</tt> object to be converted to a Long
     */
    public DefaultPmtKey(final Interval interval, final Date date) {

        if(interval == null) {
            throw new IllegalArgumentException("The Interval object must not be null.");
        }
        if(date == null) {
            throw new IllegalArgumentException("The Date object must not be null.");
        }

        key = convertDateToKey(date);
        this.interval = interval;
    }

    /**
     * Creates an object using the specified interval, date String and pattern
     * String. The Long value of the key gets converted to midnight of the date
     * value determined by the date String and pattern String. Uses
     * <tt>SimpleDateFormat</tt> to parse the date String.
     * @param interval The <tt>Interval</tt> object
     * @param dateStr The String that represents a Date that is compatible with
     * the pattern String
     * @param pattern The pattern String
     * @see SimpleDateFormat
     * @throws ParseException
     */
    public DefaultPmtKey(final Interval interval, final String dateStr, final String pattern) throws ParseException {

        if(interval == null) {
            throw new IllegalArgumentException("The Interval object must not be null.");
        }
        if(dateStr == null) {
            throw new IllegalArgumentException("The date String must not be null.");
        }
        if(pattern == null) {
            throw new IllegalArgumentException("The pattern String must not be null.");
        }

        key = convertDateStrToKey(dateStr, pattern);
        this.interval = interval;
    }

    /**
     * Creates an object using the specified key. This constructor is protected
     * and used only internally.
     * @param interval The <tt>Interval</tt> object
     * @param key The <tt>Long</tt> object
     */
    protected DefaultPmtKey(final Interval interval, final Long key) {
        
        this.key = key;
        this.interval = interval;
    }

    private static Calendar setToMidnight(final Calendar cal) {

        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private static Long convertCalendarToKey(final Calendar cal) {
        return setToMidnight(cal).getTimeInMillis();
    }

    private static Long convertDateToKey(final Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        return convertCalendarToKey(cal);
    }

    private static Long convertDateStrToKey(final String dateStr, final String pattern) throws ParseException {

        DateFormat df = new SimpleDateFormat(pattern);
        return(convertDateToKey(df.parse(dateStr)));
    }

    /**
     * Gets the <tt>Long</tt> key value encapsulated by this object.
     * @return <tt>Long</tt> key value
     */
    public Long getKey() { return key; }

    /**
     * Gets the next nth <tt>DefaultPmtKey</tt>. Returns a new instance of a
     * <tt>PmtKey</tt> object.
     * @param nth The number of times to increment the key
     * @return a new <tt>DefaultPmtKey</tt> object
     */
    public PmtKey nextNthPmtKey(int nth) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(key);
        cal.add(interval.duration(), interval.increment()*nth);
        return(new DefaultPmtKey(interval, cal.getTimeInMillis()));
    }

    /**
     * Gets the next <tt>DefaultPmtKey</tt>. Returns a new instance of a
     * <tt>PmtKey</tt> object.
     * @return a new <tt>DefaultPmtKey</tt> object
     */
    public PmtKey nextPmtKey() {

        return nextNthPmtKey(1);
    }

    /**
     * Gets the <tt>Interval</tt> enum used by this object to determine next
     * key values.
     * @return <tt>Interval</tt> object
     */
    public Interval getInterval() { return interval; }

    /**
     * Sets the <tt>Interval</tt> object. Returns a new <tt>DefaultPmtKey</tt>
     * object. The new object has the specified interval and has the same key
     * value as the object on which <tt>setInterval</tt> was called.
     * @param interval The <tt>Interval</tt> object
     * @return a new <tt>DefaultPmtKey</tt> object
     */
    public PmtKey setInterval(Interval interval) {
        return new DefaultPmtKey(interval, key);
    }

    /**
     * Returns a String representation of this object
     * @return a String representation
     */
    @Override
    public String toString() {
        return(new ToStringBuilder(this)
                .append("key", key)
                .append("interval", interval)
                .toString());
    }
}
