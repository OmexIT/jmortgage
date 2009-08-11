/*
 *  @(#)DefaultExtraPmtMapBuilder.java	1.0 2009/08/06
 *
 */
package jmortgage.payment.extra;

import jmortgage.payment.Interval;
import jmortgage.amortization.PmtKey;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * The default implementation of <tt>ExtraPmtMapBuilder</tt>. This object builds
 * an extra payment map based on the <tt>PmtKey</tt> and the number of years for
 * a mortgage term. This object is immutable as long as the implementation of
 * <tt>PmtKey</tt> it uses is immutable. <tt>DefaultPmtKey</tt> is immutable, so
 * if this object uses <tt>DefaultPmtKey</tt>, it is immutable and its thread
 * safety is guaranteed.
 * @author David Armstrong
 * @version 1.0
 */
public final class DefaultExtraPmtMapBuilder implements ExtraPmtMapBuilder {

    private final PmtKey pmtKey;
    private final int years;

    /**
     * Creates an object with the specified <tt>PmtKey</tt> implementation and
     * number of years in the mortgage term.
     * @param pmtKey the <tt>PmtKey</tt> implementation
     * @param years the number of years in the mortgage term.
     */
    public DefaultExtraPmtMapBuilder(final PmtKey pmtKey, int years) {

        if(pmtKey == null) {
            throw new IllegalArgumentException("The PmtKey object must not be null.");
        }
        if(years < 1) {
            throw new IllegalArgumentException("The years must be greater than 0.");
        }
        this.pmtKey = pmtKey;
        this.years = years;
    }

    /**
     * Creates an object with the specified <tt>PmtKey</tt> instance and
     * number of years in the mortgage term. The years value is passed in as an
     * <tt>Integer</tt> object. This constructor exists so clients
     * can instantiate this object by using
     * {@link jmortgage.JMortgageFactory#getExtraPmtMapBuilder(java.lang.Class, java.lang.Class[], java.lang.Object[])},
     * which uses reflection to build objects.
     * @param pmtKey The <tt>PmtKey</tt> instance
     * @param years The years in the mortgage
     */
    public DefaultExtraPmtMapBuilder(final PmtKey pmtKey, Integer years) {

        this(pmtKey, years.intValue());
    }

    /**
     * Builds an extra payment <tt>Map</tt> object. The map uses a <tt>Long</tt>
     * for the key that must match exactly the date of one of the regular
     * mortgage payments. The value of the map is an implementation of
     * {@link jmortgage.amortization.FixedAmortizationBuilder.Payment}. This map
     * is used by an implemenation of
     * {@link jmortgage.amortization.FixedAmortizationBuilder} to calculate the
     * effect of the extra payments on an amortization schedule.
     * @param extraPmts A <tt>List</tt> of objects that implement the
     * {@link ExtraPmt} interface. A defensive copy of this <tt>List</tt> is
     * made. Clients that need this <tt>List</tt> to be thread safe should
     * either synchronize its modification or use a thread safe implementation
     * of <tt>List</tt> like {@link java.util.concurrent.CopyOnWriteArrayList}
     * or {@link com.google.common.collect.ImmutableList}.
     * @return {@link ImmutableMap} of extra payments. The map is immutable, so
     * its thread safety is guaranteed.
     */
    public Map<Long, Double> buildExtraPmtsMap(List<ExtraPmt> extraPmts) {

        if(extraPmts == null) {
            throw new IllegalArgumentException("The extra payments List must not be null.");
        }
        List<ExtraPmt> extraPmtsCpy = null;
        // Synchronize the copy to the object's list of extra payments in case of the very slight chance that another thread is trying to access the passed in list at the same time.
        // Note: For true thread safety, the client would need to synchronize his access to the list if he modifies it in another thread.
        synchronized(extraPmts) {
            // We are doing a shallow copy of each element in the list. This is fine as long as the implementation of ExtraPmt is immutable, which DefaultExtraPmt is.
            // But, if the implementation of ExtraPmt is mutable, this approach is not thread safe.
            extraPmtsCpy = ImmutableList.copyOf(extraPmts);
        }

        ImmutableMap.Builder<Long, Double> extraPmtsMapBuilder;
        int extraPmtsCt = extraPmtsCpy.size();
        if(extraPmtsCt > 0) {
            extraPmtsMapBuilder = ImmutableMap.builder();
            List<MutableExtraPmt> adders = new ArrayList<MutableExtraPmt>(extraPmtsCt);
            for(ExtraPmt extraPmt: extraPmtsCpy) {
                adders.add(new MutableExtraPmt(extraPmt));
            }

            PmtKey next = pmtKey.nextPmtKey();
            int pmtCt = years * pmtKey.getInterval().pmtsPerYear();

            do {
                pmtCt--;
                Long key = next.getKey();
                double amount = 0;

                for(int i = 0; i < extraPmtsCt; i++) {
                    MutableExtraPmt adder = adders.get(i);
                    if(key.equals(adder.pmtKey.getKey()) && adder.count > 0) {
                        amount += adder.amount;
                        adder.incrementDate();
                        adder.decrementCount();
                    } else if(adder.count == 0) {
                        adders.remove(i);
                        extraPmtsCt--;
                        i--;
                    }
                }
                next = next.nextPmtKey();

                if(amount > 0) {
                    extraPmtsMapBuilder.put(key, amount);
                }

            } while(pmtCt > 0 && extraPmtsCt > 0);

        } else {
            extraPmtsMapBuilder = null;
        }

        // Return an ImmutableMap object.
        return extraPmtsMapBuilder != null ? extraPmtsMapBuilder.build() : null;
    }

    /**
     * Gets the <tt>PmtKey</tt> instantiation used to build the map of extra
     * payments. The <tt>PmtKey</tt> instantiation must be the same as the one
     * used to build the amortization table with an instantiation of
     * <tt>FixedAmortizationBuilder</tt> to which the extra payments will apply.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey() { return pmtKey; }

     /**
     * Gets the years in the mortgage term used to build the map of extra
     * payments. The year value must be the same as the one used to build an
     * amortization table with an instantiation of <tt>FixedAmortizationBuilder</tt>
     * to which the extra payments will apply.
     * @return <tt>int</tt> years value
     */
   public int getYears() { return years; }

    /**
     * Sets the <tt>PmtKey</tt> instantiation. This method returns a new
     * instance of <tt>DefaultExtraPmtMapBuilder</tt>
     * @param pmtKey <tt>PmtKey</tt> object to be used to build a map of extra
     * payments
     * @return <tt>DefaultExtraPmtMapBuilder</tt> instantiation
     */
    public ExtraPmtMapBuilder setPmtKey(PmtKey pmtKey) { return new DefaultExtraPmtMapBuilder(pmtKey, this.years); }

    /**
     * Sets the years in the mortgage for which the <tt>ExtraPmtMapBuilder</tt>
     * instance will build a map of extra payments. This method returns a new
     * instance of <tt>DefaultExtraPmtMapBuilder</tt>
     * @param years number of years in the mortgage
     * @return <tt>DefaultExtraPmtMapBuilder</tt> instantiation
     */
    public ExtraPmtMapBuilder setYears(int years) { return new DefaultExtraPmtMapBuilder(this.pmtKey, years); }

    /**
     * Returns a String representation of this object.
     * @return a String representation
     */
    @Override
    public String toString() {
        return(new ToStringBuilder(this)
                .append("pmtKey", pmtKey)
                .append("years", years)
                .toString());
    }

    private class MutableExtraPmt {

        private PmtKey pmtKey;
        private int count;
        private final double amount;

        public MutableExtraPmt(final ExtraPmt extraPmt) {
            pmtKey = extraPmt.getPmtKey();
            count = extraPmt.getCount();
            amount = extraPmt.getAmount();
        }

        public void incrementDate() {

            if(!pmtKey.getInterval().equals(Interval.Onetime)) {
                pmtKey = pmtKey.nextPmtKey();
            }
        }

        public void decrementCount() {
            count--;
        }

        @Override
        public String toString() {
            return(new ToStringBuilder(this)
                    .append("pmtKey", pmtKey)
                    .append("count", count)
                    .append("amount", amount)
                    .toString());
        }

    }

}
