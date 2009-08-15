/*
 *  @(#)ExtraPmtMapBuilder.java	1.0 2009/08/06
 *
 */
package jmortgage.payment.extra;

import java.util.Map;
import java.util.List;
import jmortgage.amortization.PmtKey;

/**
 * Implementation of this interface builds an extra payment map based on the
 * <tt>PmtKey</tt> and the number of years for a mortgage term.
 * @since 1.0
 * @author David Armstrong
 */
public interface ExtraPmtMapBuilder {

    /**
     * Builds an extra payment <tt>Map</tt> object. The map uses a <tt>Long</tt>
     * for the key that must match exactly the date of one of the regular
     * mortgage payments. The value of the map is an implementation of
     * {@link jmortgage.amortization.FixedAmortizationBuilder.Payment}. This map is used by an
     * implemenation of {@link jmortgage.amortization.FixedAmortizationBuilder}
     * to calculate the effect of the extra payments on an amortization schedule.
     * @param extraPmts A <tt>List</tt> of objects that implement the
     * {@link ExtraPmt} interface.
     * @return <tt>Map</tt> of extra payments
     */
    public Map<Long, Double> buildExtraPmtsMap(List<ExtraPmt> extraPmts);

    /**
     * Gets the <tt>PmtKey</tt> instantiation used to build the map of extra
     * payments. The <tt>PmtKey</tt> instantiation must be the same as the one
     * used to build the amortization table with an instantiation of
     * <tt>FixedAmortizationBuilder</tt> to which the extra payments will apply.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey();

    /**
     * Gets the years in the mortgage term used to build the map of extra
     * payments. The year value must be the same as the one used to build an
     * amortization table with an instantiation of <tt>FixedAmortizationBuilder</tt>
     * to which the extra payments will apply.
     * @return <tt>int</tt> years value
     */
    public int getYears();

    /**
     * Sets the <tt>PmtKey</tt> instantiation. This method encourages
     * immutability by returning an instance of <tt>ExtraPmtMapBuilder</tt>
     * @param pmtKey <tt>PmtKey</tt> object to be used to build a map of extra
     * payments
     * @return <tt>ExtraPmtMapBuilder</tt> instantiation
     */
    public ExtraPmtMapBuilder setPmtKey(PmtKey pmtKey);

    /**
     * Sets the years in the mortgage for which the <tt>ExtraPmtMapBuilder</tt>
     * instance will build a map of extra payments. This method encourages
     * immutability by returning an instance of <tt>ExtraPmtMapBuilder</tt>
     * @param years number of years in the mortgage
     * @return <tt>ExtraPmtMapBuilder</tt> instantiation
     */
    public ExtraPmtMapBuilder setYears(int years);
}
