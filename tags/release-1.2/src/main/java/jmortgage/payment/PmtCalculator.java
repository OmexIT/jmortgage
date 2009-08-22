/*
 *  @(#)PmtCalculator.java	1.0 2009/08/06
 *
 */

package jmortgage.payment;

/**
 * A payment calculator for a fixed mortgage. An implementation of this
 * interface calculates the interval payment of a fixed mortgage given the
 * interest rate, loan amount, and length of mortgage term in years. The
 * interval between each payment is defined by {@link Interval}.
 *
 * @since 1.0
 * @author David Armstrong
 */
public interface PmtCalculator {

    /**
     * Gets the loan amount used to determine the payment.
     * @return loan amount as <tt>double</tt>.
     */
    public double getLoanAmt();

    /**
     * Gets the interest rate used to determine the payment.
     * @return interest rate as <tt>double</tt>.
     */
    public double getInterestRate();

    /**
     * Gets the interval interest rate used to determine the payment. This value
     * is used by implementations of {@link jmortgage.amortization.DefaultFixedAmortizationBuilder} when
     * building an amortization table.
     * @return interval interest rate as <tt>double</tt>.
     */
    public double getIntervalInterestRate();

    /**
     * Gets the payment count used to determine the payment. For a 30 year loan
     * with a monthly payment interval, this value would be 360.
     * @return payment count as <tt>int</tt>.
     */
    public int getPmtCt();

    /**
     * Gets the duration of the mortgage term in years.
     * @return years as <tt>int</tt>.
     */
    public int getYears();

    /**
     * Gets the interval used to determine the payment. The interval can be
     * weekly, biweekly, or monthly.
     * @return {@link Interval}
     */
    public Interval getInterval();

    /**
     * Sets the loan amount used to determine the payment. This method
     * encourages implementations to be immutable by specifying that it
     * returns an object that implements <tt>PmtCalculator</tt>.
     * @param loanAmt the loan amount as <tt>double</tt> to be used to calculate
     * the payment.
     * @return {@link PmtCalculator}.
     */
    public PmtCalculator setLoanAmt(double loanAmt);

    /**
     * Sets the interest rate used to determine the payment. This method
     * encourages implementations to be immutable by specifying that it
     * returns an object that implements <tt>PmtCalculator</tt>.
     * @param interestRate the interest rate as <tt>double</tt> to be used to
     * calculate the payment.
     * @return {@link PmtCalculator}.
     */
    public PmtCalculator setInterestRate(double interestRate);

    /**
     * Sets the years used to determine the payment. This method
     * encourages implementations to be immutable by specifying that it
     * returns an object that implements <tt>PmtCalculator</tt>.
     * @param years as <tt>int</tt> to be used to calculate the payment.
     * @return {@link PmtCalculator}.
     */
    public PmtCalculator setYears(int years);


    /**
     * Sets the interval used to determine the payment. This method
     * encourages implementations to be immutable by specifying that it
     * returns an object that implements <tt>PmtCalculator</tt>.
     * @param interval {@link Interval} object to be used to calculate the
     * payment.
     * @return {@link PmtCalculator}.
     */
    public PmtCalculator setInterval(Interval interval);

    /**
     * Calculates the payment. Implementations of this method should return
     * the payment amount rounded.
     *
     * @return payment rounded as <tt>double</tt>.
     */
    public double calcPmt();

    /**
     * Calculates the payment and returns it unrounded. This method is used by
     * implementations of <tt>FixedAmortizationBuilder</tt> when building an
     * amortization schedule.
     * @return payment as <tt>double</tt>.
     */
    public double calcPmtUnrounded();
}
