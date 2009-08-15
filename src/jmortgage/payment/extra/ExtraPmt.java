/*
 *  @(#)ExtraPmt.java	1.0 2009/08/08
 *
 */
package jmortgage.payment.extra;

import jmortgage.amortization.PmtKey;

/**
 * Implementations of this interface encapsulate extra payments. Extra payment
 * objects contain the amount of the extra payment, the number of payments
 * to which the extra payment will be applied, and a PmtKey object. The PmtKey
 * object indicates the start date of the extra payment. This start date must
 * match exactly with the date of one of the regular mortgage payments. The
 * PmtKey also indicates the interval of the extra payment. Valid interval
 * values are Onetime, Yearly, Monthly, Biweekly, and Weekly.
 * @since 1.0
 * @author David Armstrong
 */
public interface ExtraPmt {

    /**
     * Gets the <tt>PmtKey</tt> object for this extra payment. The <tt>PmtKey</tt>
     * object indicates the start date of the extra payment and the interval
     * between extra payments.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey();

    /**
     * Gets the number of consecutive times to apply the extra payment.
     * Consecutive is defined by the interval value indicated in the <tt>PmtKey</tt>
     * object.
     * @return <tt>int</tt>
     */
    public int getCount();

    /**
     * Gets the amount of the extra payment.
     * @return <tt>double</tt>
     */
    public double getAmount();

    /**
     * Sets the <tt>PmtKey</tt> instance. This method encourages immutability by
     * returning an instance of <tt>ExtraPmt</tt>. The object returned will use
     * the specified <tt>PmtKey</tt> instance and will have the same count and
     * amount values as the object on which <tt>setPmtKey</tt> was called.
     * @param pmtKey <tt>PmtKey</tt> instance
     * @return <tt>ExtraPmt</tt> instantiation
     */
    public ExtraPmt setPmtKey(final PmtKey pmtKey);

    /**
     * Sets the count. This method encourages immutability by returning an
     * instance of <tt>ExtraPmt</tt>. The object returned will use the
     * specified count and will have the same <tt>ExtraPmt</tt> instance and
     * amount values as the object on which <tt>setCount</tt> was called.
     * @param count The count value
     * @return <tt>ExtraPmt</tt> instantiation
     */
    public ExtraPmt setCount(int count);

    /**
     * Sets the amount. This method encourages immutability by returning an
     * instance of <tt>ExtraPmt</tt>. The object returned will use the
     * specified amount and will have the same <tt>ExtraPmt</tt> instance and
     * count values as the object on which <tt>setAmount</tt> was called.
     * @param amount The amount value
     * @return <tt>ExtraPmt</tt> instantiation
     */
    public ExtraPmt setAmount(double amount);

}
