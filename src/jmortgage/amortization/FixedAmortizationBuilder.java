/*
 *  @(#)FixedAmortizationBuilder.java	1.0 2009/08/06
 *
 */
package jmortgage.amortization;

import jmortgage.payment.PmtCalculator;
import jmortgage.payment.extra.ExtraPmt;
import java.util.SortedMap;
import java.util.Map;
import java.util.List;

/**
 * An amortization table builder for a fixed mortgage. An implementation of this
 * builds a {@link SortedMap}
 * that holds the contents of an amortization schedule. The map uses a Long
 * object that represents the date of the payment as the key and an object that
 * implements the {@link Payment} interface as the value.
 *
 * @author David Armstrong
 *
 * @version 1.0
 *
 */
public interface FixedAmortizationBuilder {

    /**
     * Gets the implementation of <tt>PmtCalculator</tt> used to build the amortization
     * table.
     * @return <tt>PmtCalculator</tt> instantiation
     */
    public PmtCalculator getPmtCalculator();

    /**
     * Gets the implementation of <tt>PmtKey</tt> used to build the amortization table.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey();

    /**
     * Sets the implementation of <tt>PmtCalculator</tt> to use to build the amortization
     * table. This method encourages immutability by returning an object that
     * instantiates <tt>FixedAmortizationBuilder</tt>
     * @param pmtCalc The <tt>PmtCalculator</tt> instantiation to use to build the
     * amortization table.
     * @return <tt>FixedAmortizationBuilder</tt> instantiation.
     */
    public FixedAmortizationBuilder setPmtCalculator(PmtCalculator pmtCalc);

    /**
     * Sets the implementation of <tt>PmtKey</tt> to use to build the amortization
     * table. This method encourages immutability by returning an object that
     * instantiates <tt>FixedAmortizationBuilder</tt>
     * @param pmtKey The <tt>PmtKey</tt> instantiation to use to build the
     * amortization table.
     * @return <tt>FixedAmortizationBuilder</tt> instantiation.
     */
    public FixedAmortizationBuilder setPmtKey(PmtKey pmtKey);

    /**
     * Builds the amortization table. The amortization table is represented as
     * a <tt>SortedMap</tt> with a <tt>Long</tt> that represents the date the payment
     * is due as the key and an implementation of
     * <tt>FixedAmortizationBuilder.Payment</tt> as the value.
     * @return <tt>SortedMap</tt>
     */
    public SortedMap<Long, Payment> buildAmortizationTable();

    /**
     * Builds the amortization table. The amortization table is represented as
     * a <tt>SortedMap</tt> with a <tt>Long</tt> that represents the date the payment
     * is due as the key and an implementation of
     * <tt>FixedAmortizationBuilder.Payment</tt> as the value.
     * @param extraPmtMap A map of extra payments. The map uses a <tt>Long</tt> that
     * represents the date the extra payment is to be applied. This date must
     * correspond exactly with one of the scheduled payments in the amortization
     * table. The value is a <tt>Double</tt> object that represents the extra payment
     * value.
     * @return <tt>SortedMap</tt>
     */
    public SortedMap<Long, Payment> buildAmortizationTable(final Map<Long, Double> extraPmtMap);

    /**
     * Builds the amortization table. The amortization table is represented as
     * a <tt>SortedMap</tt> with a <tt>Long</tt> that represents the date the payment
     * is due as the key and an implementation of
     * <tt>FixedAmortizationBuilder.Payment</tt> as the value.
     * @param extraPmtList A list of objects that implement ExtraPmt. This list
     * is then processed internally by an implementation of
     * {@link jmortgage.payment.extra.ExtraPmtMapBuilder}. This is a convenience method to save users
     * the trouble of explicitly instantiating an implementation of
     * <tt>ExtraPmtMapBuilder</tt> to process a list of instantiations of <tt>ExtraPmt</tt>
     * objects themselves.
     * @return <tt>SortedMap</tt>
     */
    public SortedMap<Long, Payment> buildAmortizationTable(final List<ExtraPmt> extraPmtList);

    /**
     * A payment object. Instantiations of this interface calculate the total
     * amount of the payment, the principal paid, the interest paid, the balance
     * due after the payment, and the cumulative interest paid after the
     * payment.
     */
    public interface Payment {

        /**
         * Gets the total amount paid for this payment
         * @return <tt>double</tt> The total amount of payment
         */
        public double getTotal();

        /**
         * Gets the principal amount paid for this payment
         * @return <tt>double</tt> The principal paid.
         */
        public double getPrincipal();

        /**
         * Gets the interest amount paid for this payment.
         * @return <tt>double</tt> The interest paid.
         */
        public double getInterest();

        /**
         * Gets the balance due after this payment.
         * @return <tt>double</tt> The balance due.
         */
        public double getBalance();

        /**
         * Gets the cumulative interest paid after this payment
         * @return <tt>double</tt> The cumulative interest paid.
         */
        public double getCumulativeInterest();

        /**
         * Gets the total amount paid for the payment unrounded.
         * @return <tt>double</tt> the unrounded total amount paid.
         */
        public double getTotalUnrounded();

        /**
         * Gets the principal amount paid for this payment unrounded.
         * @return <tt>double</tt> The principal paid.
         */
        public double getPrincipalUnrounded();

        /**
         * Gets the interest amount paid for this payment unrounded.
         * @return <tt>double</tt> The interest paid.
         */
        public double getInterestUnrounded();

        /**
         * Gets the balance due after this payment unrounded.
         * @return <tt>double</tt> The balance due.
         */
        public double getBalanceUnrounded();

        /**
         * Gets the cumulative interest paid after this payment unrounded.
         * @return <tt>double</tt> The cumulative interest paid.
         */
        public double getCumulativeInterestUnrounded();
    }
}
