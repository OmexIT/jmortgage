/*
 *  @(#)PmtKey.java	1.0 2009/08/08
 *
 */
package jmortgage.amortization;

import jmortgage.payment.Interval;

/**
 * The payment key calculator used in an amortization map built by an
 * implementation of {@link FixedAmortizationBuilder}. The key used in
 * an amortization map is a Long value that represents the date that the
 * payment is due. The {@link Interval} value represents the time between
 * payments. Implementations of this interface provide methods to
 * calculate the first key and to calculate subsequent keys. Calculation of
 * subsequent keys is based on the interval value.
 *
 * @author David Armstrong
 *
 * @version 1.0
 *
 */
public interface PmtKey {

    /**
     * Gets the <tt>Long</tt> key value that this implementation of PmtKey
     * encapsulates.
     * @return <tt>Long</tt>
     */
    public Long getKey();

    /**
     * Gets the next nth PmtKey. This method encourages immutability by
     * returning an instance of PmtKey.
     * @param nth The number of keys to add to the key value in the PmtKey
     * instance
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey nextNthPmtKey(int nth);
    
    /**
     * Gets the next PmtKey. This method encourages immutability by returning an
     * instance of PmtKey.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey nextPmtKey();

    /**
     * Gets the <tt>Interval</tt> enum used to determine the length of time
     * between keys.
     * @return <tt>Interval</tt> enum
     */
    public Interval getInterval();

    /**
     * Sets the <tt>Interval</tt> enum to be used to determine the length of
     * time between keys. This method encourages immutability by returning an
     * instance of PmtKey
     * @param interval <tt>Interval</tt> enum
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey setInterval(Interval interval);

}
