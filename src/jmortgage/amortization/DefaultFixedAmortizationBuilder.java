/*
 *  @(#)DefaultFixedAmortizationBuilder.java	1.0 2009/08/06
 *
 */
package jmortgage.amortization;

import jmortgage.JMortgageFactory;
import jmortgage.payment.PmtCalculator;
import jmortgage.payment.extra.ExtraPmt;
import java.util.SortedMap;
import java.util.Map;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableMap;

/**
 * The default implementation of <tt>FixedAmortizationBuilder</tt>. This object
 * builds an amortization schedule based on the values of the
 * <tt>PmtCalculator</tt> and <tt>PmtKey</tt> implementations it receives. When
 * building the amortization schedule, it can receive extra payments that are
 * calculated into the amortization schedule. This object is immutable <b>as
 * long as</b> the <tt>PmtCalculator</tt> and <tt>PmtKey</tt> implementations it
 * receives are immutable. {@link jmortgage.payment.DefaultPmtCalculator} and
 * {@link DefaultPmtKey} are immutable, so if these implementations are used,
 * this object is immutable, so its thread safety is  guaranteed.
 * Note: While thread safety of the object itself would be guaranteed, two of
 * the buildAmortizationTable methods take <tt>Collection</tt> arguments that
 * represent extra payments and these objects may need to be synchronized to
 * guarantee their thread safety.
 * @author David Armstrong
 * @version 1.0
 */
public final class DefaultFixedAmortizationBuilder implements FixedAmortizationBuilder {

    private final PmtCalculator pmtCalc;
    private final PmtKey pmtKey;
    // Get a local copy of intervalInterestRate, mthlyPmt, and interval from the PmtCalculator object because they will be used so much when building the amortization table.
    private final double intervalInterestRate;
    private final double mthlyPmt;

    /**
     * Creates an object with the specified <tt>PmtCalculator</tt> and
     * <tt>PmtKey</tt> instantiations.
     * @param pmtCalc The <tt>PmtCalculator</tt> instantiation
     * @param pmtKey The <tt>PmtKey</tt> instantiation
     */
    public DefaultFixedAmortizationBuilder(final PmtCalculator pmtCalc, final PmtKey pmtKey) {

        if(pmtCalc == null) {
            throw new IllegalArgumentException("The PmtCalculator object must not be null.");
        }
        if(pmtKey == null) {
            throw new IllegalArgumentException("The PmtKey object must not be null.");
        }
        this.pmtCalc = pmtCalc;
        this.intervalInterestRate = this.pmtCalc.getIntervalInterestRate();
        this.mthlyPmt = this.pmtCalc.calcPmtUnrounded();
        this.pmtKey = pmtKey;
    }

    /**
     * Gets the <tt>PmtKey</tt> instantiation used to create this object.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey() { return pmtKey; }

    /**
     * Gets the <tt>PmtCalculator</tt> instantiation used to create this object.
     * @return <tt>PmtCalculator</tt> instantiation
     */
    public PmtCalculator getPmtCalculator() { return pmtCalc; }

    /**
     * Sets the <tt>PmtKey</tt> instantiation for a new
     * <tt>FixedAmortizationBuilder</tt> object. The new object is built with
     * the specified <tt>PmtKey</tt> object and the same <tt>PmtCalculator</tt>
     * object as the one on which <tt>setPmtKey</tt> was called.
     * @param pmtKey The <tt>PmtKey</tt> object to be used to build a new
     * <tt>FixedAmortizationBuilder</tt> object
     * @return a new <tt>FixedAmortizationBuilder</tt> object
     */
    public FixedAmortizationBuilder setPmtKey(PmtKey pmtKey) { return new DefaultFixedAmortizationBuilder(this.pmtCalc, pmtKey); }

    /**
     * Sets the <tt>PmtCalculator</tt> instantiation for a new
     * <tt>FixedAmortizationBuilder</tt> object. The new object is built with
     * the specified <tt>PmtCalculator</tt> object and the same <tt>PmtKey</tt>
     * object as the one on which <tt>setPmtCalculator</tt> was called.
     * @param pmtCalc The <tt>PmtCalculator</tt> object to be used to build a
     * new <tt>FixedAmortizationBuilder</tt> object
     * @return a new <tt>FixedAmortizationBuilder</tt> object
     */
    public FixedAmortizationBuilder setPmtCalculator(PmtCalculator pmtCalc) { return new DefaultFixedAmortizationBuilder(pmtCalc, this.pmtKey); }

    /**
     * Builds the amortization table. The table is represented as a <tt>SortedMap</tt>
     * that is sorted in chronological order. The key of the map is a <tt>Long</tt>
     * value representing the date that the payment is due. The value is a
     * <tt>DefaultFixedAmortizationBuilder.DefaultPayment</tt> object.
     * <tt>DefaultFixedAmortizationBuilder</tt> uses Google Collection
     * Library's {@link ImmutableSortedMap} to encapsulate the amortization
     * table. Use of this implementation is immutable, guaranteeing the return
     * value's thread safety.
     * @return amortization table as an <tt>ImmutableSortedMap</tt> object.
     */
    public SortedMap<Long, Payment> buildAmortizationTable() {

        double principalOwed = pmtCalc.getLoanAmt();
        double interestPaid = 0.0;
        PmtKey next = pmtKey.nextPmtKey();
        Long key = next.getKey();
        ImmutableSortedMap.Builder<Long, Payment> pmtMapBuilder = ImmutableSortedMap.naturalOrder();
        int pmtCt = pmtCalc.getPmtCt();

        while((new BigDecimal(principalOwed).setScale(2,RoundingMode.HALF_EVEN).doubleValue() > 0.0) && (pmtCt-- > 0)) {
            Payment payment = new DefaultPayment(principalOwed, interestPaid);
            pmtMapBuilder.put(key, payment);
            principalOwed = payment.getBalanceUnrounded();
            interestPaid = payment.getCumulativeInterestUnrounded();
            next = next.nextPmtKey();
            key = next.getKey();
        }

        // Return an ImmutableSortedMap
        return pmtMapBuilder.build();
    }

    /**
     * Builds the amortization table and processes the specified map of extra
     * payments. A defensive copy of the extra payments is made. Clients who
     * need to ensure thread safety of the map of extra payments should
     * synchronize its modification in another thread or use a thread-safe
     * implementation of a map like {@link java.util.concurrent.ConcurrentHashMap}
     * or Google's {@link com.google.common.collect.ImmutableMap} in case of the
     * slight chance that a modification of the map occurs at the same time the
     * defensive copy is made. The table is represented as a <tt>SortedMap</tt>
     * that is sorted in chronological order. The key of the map is a
     * <tt>Long</tt> value representing the date that the payment is due. The
     * value is a <tt>DefaultFixedAmortizationBuilder.DefaultPayment</tt> object.
     * <tt>DefaultFixedAmortizationBuilder</tt> uses Google Collection
     * Library's <tt>ImmutableSortedMap</tt> to encapsulate the amortization
     * table. Use of this implementation is immutable, guaranteeing the return
     * value's thread safety.
     * @param extraPmtMap A map of extra payments. The key is a Long that
     * represents the date that the extra payment will be paid. This date must
     * match exactly with one of the regular payments in the amortization
     * schedule.
     * @return amortization table as an <tt>ImmutableSortedMap</tt> object
     */
    public SortedMap<Long, Payment> buildAmortizationTable(final Map<Long, Double> extraPmtMap) {

        if(extraPmtMap == null) {
            throw new IllegalArgumentException("The extra payments map must not be null.");
        }

        Map<Long, Double> extraPmtsCpy = null;
        // Synchronize the copy to the object's map of extra payments in case of the very slight chance that another thread is trying to access the passed in map at the same time.
        // Note: For true thread safety, the client would need to synchronize his access to the map if he is modifying it in another thread.
        synchronized(extraPmtMap) {
            // A shallow copy of the Map is fine since Long and Double are immutable.
            //extraPmtsCpy = new HashMap<Long, Double>(extraPmtMap);
            extraPmtsCpy = ImmutableMap.copyOf(extraPmtMap);
        }
        double principalOwed = pmtCalc.getLoanAmt();
        double interestPaid = 0.0;
        PmtKey next = pmtKey.nextPmtKey();
        Long key = next.getKey();
        ImmutableSortedMap.Builder<Long, Payment> pmtMapBuilder = ImmutableSortedMap.naturalOrder();
        int pmtCt = pmtCalc.getPmtCt();

        while((new BigDecimal(principalOwed).setScale(2,RoundingMode.HALF_EVEN).doubleValue() > 0.0) && (pmtCt-- > 0)) {
            Double extraPmt = extraPmtsCpy.get(key);
            Payment payment = null;
            if(extraPmt != null) {
                payment = new DefaultPayment(principalOwed, interestPaid, extraPmt.doubleValue());
            } else {
                payment = new DefaultPayment(principalOwed, interestPaid);
            }
            pmtMapBuilder.put(key, payment);
            principalOwed = payment.getBalanceUnrounded();
            interestPaid = payment.getCumulativeInterestUnrounded();
            next = next.nextPmtKey();
            key = next.getKey();
        }

        // Return an ImmutableSortedMap.
        return pmtMapBuilder.build();
    }

    /**
     * Builds the amortization table and processes the specified list of extra
     * payments which are represented as implementations of {@link ExtraPmt}.
     * A defensive copy of the extra payments is made. Clients who need to
     * ensure thread safety of the list of extra payments should synchronize
     * its modification in another thread or use a thread-safe implementation
     * of a list like {@link java.util.concurrent.CopyOnWriteArrayList} or
     * Google's {@link com.google.common.collect.ImmutableList}
     * in case of the slight chance that a modification of the list occurs at
     * the same time the defensive copy is made. This method is a convenience
     * method to save the client the trouble of creating an implementation of
     * {@link jmortgage.payment.extra.ExtraPmtMapBuilder}. If a client is
     * planning to use the same extra payments for more than one amortization
     * schedule, it should create a map of extra payments itself with an
     * implementation of <tt>ExtraPmtMapBuilder</tt>. The table is represented
     * as a <tt>SortedMap</tt> that is sorted in chronological order. The key of
     * the map is a <tt>Long</tt> value representing the date that the payment
     * is due. The value is a
     * <tt>DefaultFixedAmortizationBuilder.DefaultPayment</tt> object.
     * <tt>DefaultFixedAmortizationBuilder</tt> uses Google Collection
     * Library's <tt>ImmutableSortedMap</tt> to encapsulate the amortization
     * table. Use of this implementation is immutable, guaranteeing the return
     * value's thread safety.
     * @param extraPmtList A <tt>List of extra payments represented as
     * <tt>ExtraPmt</tt> implementations
     * @return amortization table as an <tt>ImmutableSortedMap</tt> object
     */
    public SortedMap<Long, Payment> buildAmortizationTable(final List<ExtraPmt> extraPmtList) {

        return(buildAmortizationTable(JMortgageFactory.getDefaultExtraPmtMapBuilder(pmtKey, pmtCalc.getYears()).buildExtraPmtsMap(extraPmtList)));
    }

    /**
     * Returns a String representation of this object.
     * @return a String representation
     */
    @Override
    public String toString() {
        return(new ToStringBuilder(this)
                .append("pmtCalc", pmtCalc)
                .append("intervalInterestRate", intervalInterestRate)
                .append("mthlyPmt", mthlyPmt)
                .append("pmtKey", pmtKey)
                //.append("payments", payments)
                .toString());
    }


    // This does not work if you round the numbers for each iteration. Instead, you have to round the numbers after they have been calculated in this object when you display them.
    // Then, it looks right.
    /**
     * The default implementation of <tt>FixedAmortizationBuilder.Payment</tt>.
     * This object calculates the total amount of the payment, the principal
     * paid, the interest paid, the balance due after the payment, and the
     * cumulative interest paid after the payment.
     */
    public final class DefaultPayment implements FixedAmortizationBuilder.Payment {

        private final double total; // the total amount paid for this payment
        private final double principal; // the principal paid for this payment
        private final double interest; // the interest paid for this payment
        private final double cumulativeInterest; // the cumulative interest paid after this payment
        private final double balance; // the balance due after this payment

        /**
         * Creates an object with the specified principal owed, interest paid,
         * and extra payment.
         * @param principalOwed The principal owed before this payment
         * @param interestPaid The interest paid before this payment
         * @param extraPmt The extra payment amount for this payment
         */
        public DefaultPayment(double principalOwed, double interestPaid, double extraPmt) {

            // The extraPmt is the extra amount being paid for this payment. The total is the monthly payment plus the extra payment.
            interest = principalOwed * intervalInterestRate;
            total = Math.min(principalOwed + interest, mthlyPmt + extraPmt);
            principal = total - interest;
            balance = principalOwed - principal;
            cumulativeInterest = interestPaid + interest;
        }

        /**
         * Creates an object with the specified principal owed andinterest paid.
         * @param principalOwed The principal owed before this payment
         * @param interestPaid The interest paid before this payment
         */
        public DefaultPayment(double principalOwed, double interestPaid) {

            this(principalOwed, interestPaid, 0);
        }

        /**
         * Gets the total amount paid for this payment rounded
         * @return total amount paid rounded
         */
        public double getTotal() { return new BigDecimal(total).setScale(2,RoundingMode.HALF_EVEN).doubleValue(); }

        /**
         * Gets the total principal paid for this payment rounded
         * @return total principal paid rounded
         */
        public double getPrincipal() { return new BigDecimal(principal).setScale(2,RoundingMode.HALF_EVEN).doubleValue(); }

        /**
         * Gets the total interest paid for this payment rounded
         * @return total interest paid rounded
         */
        public double getInterest() { return new BigDecimal(interest).setScale(2,RoundingMode.HALF_EVEN).doubleValue(); }

        /**
         * Gets the total balance due after this payment rounded
         * @return total balance after payment rounded
         */
        public double getBalance() { return new BigDecimal(balance).setScale(2,RoundingMode.HALF_EVEN).doubleValue(); }

        /**
         * Gets the cumulative interest paid after this payment rounded
         * @return cumulative interest after payment rounded
         */
        public double getCumulativeInterest() { return new BigDecimal(cumulativeInterest).setScale(2,RoundingMode.HALF_EVEN).doubleValue(); }

        /**
         * Gets the total amount paid for this payment unrounded
         * @return total amount paid unrounded
         */
        public double getTotalUnrounded() { return total; }

        /**
         * Gets the total principal paid for this payment unrounded
         * @return total principal paid unrounded
         */
        public double getPrincipalUnrounded() { return principal; }

        /**
         * Gets the total interest paid for this payment unrounded
         * @return total interest paid unrounded
         */
        public double getInterestUnrounded() { return interest; }

        /**
         * Gets the total balance due after this payment unrounded
         * @return total balance after payment unrounded
         */
        public double getBalanceUnrounded() { return balance; }

        /**
         * Gets the cumulative interest paid after this payment unrounded
         * @return cumulative interest after payment unrounded
         */
        public double getCumulativeInterestUnrounded() { return cumulativeInterest; }

        /**
         * Returns a String representation of this object
         * @return a String representation
         */
        @Override
        public String toString() {
            return(new ToStringBuilder(this)
                    .append("total", total)
                    .append("principal", principal)
                    .append("interest", interest)
                    .append("cumulativeInterest", cumulativeInterest)
                    .append("balance", balance)
                    .toString());
        }
    }
}
