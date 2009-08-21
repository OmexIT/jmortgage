/*
 *  @(#)DefaultPmiCalculator.java	1.2 2009/08/20
 *
 */

package jmortgage.pmi;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A simple PMI calculator. The amount to pay is based on the percentage of the
 * home's value paid down. If the down payment is less than 20%, the borrower
 * has to pay a monthly PMI amount.
 *
 * @since 1.2
 * @author David Armstrong
 */
public class DefaultPmiCalculator implements PmiCalculator {

    /**
     * The default amount by which to divide the loan amount if the down payment
     * amount is less than 10 percent.
     */
    public static final double DFLT_FIVE_PCT_DIVIDER = 1500;

    /**
     * The default amount by which to divide the loan amount if the down payment
     * amount is less than 15 percent and more than or equal to 10 percent.
     */
    public static final double DFLT_TEN_PCT_DIVIDER = 2300;

    /**
     * The default amount by which to divide the loan amount if the down payment
     * amount is less than 20 percent and more than or equal to 15 percent.
     */
    public static final double DFLT_FIFTEEN_PCT_DIVIDER = 3700;

    private final double fivePctDivider;
    private final double tenPctDivider;
    private final double fifteenPctDivider;

    /**
     * Creates a new instance of <tt>DefaultPmiCalculator</tt> using the default
     * values for five, ten, and fifteen percent down dividers.
     */
    public DefaultPmiCalculator() {

        fivePctDivider = DFLT_FIVE_PCT_DIVIDER;
        tenPctDivider = DFLT_TEN_PCT_DIVIDER;
        fifteenPctDivider = DFLT_FIFTEEN_PCT_DIVIDER;
    }

    /**
     * Creates a new instance of <tt>DefaultPmiCalculator</tt> with the
     * specified five, ten, and fifteen percent dividers. This constructor is
     * available for the benefit of {@link jmortgage.JMortgageFactory}, which
     * uses reflection to build objects. Reflection will not find constructors
     * that take primitives as arguments.
     * @param fivePctDivider
     * @param tenPctDivider
     * @param fifteenPctDivider
     */
    public DefaultPmiCalculator(Double fivePctDivider, Double tenPctDivider, Double fifteenPctDivider) {

        this(fivePctDivider.doubleValue(), tenPctDivider.doubleValue(), fifteenPctDivider.doubleValue());
    }

    /**
     * Creates a new instance of <tt>DefaultPmiCalculator</tt> with the
     * specified five, ten, and fifteen percent dividers.
     * @param fivePctDivider
     * @param tenPctDivider
     * @param fifteenPctDivider
     */
    public DefaultPmiCalculator(double fivePctDivider, double tenPctDivider, double fifteenPctDivider) {
        
        this.fivePctDivider = fivePctDivider;
        this.tenPctDivider = tenPctDivider;
        this.fifteenPctDivider = fifteenPctDivider;
    }

    public double calcPmi(double homeValue, double amtDown) {

        double pmiAmt;
        double downPct = (amtDown * 100)/homeValue;
        if(downPct < 10) {
            pmiAmt = (homeValue - amtDown)/fivePctDivider;
        } else if(downPct < 15) {
            pmiAmt = (homeValue - amtDown)/tenPctDivider;
        } else if(downPct < 20) {
            pmiAmt = (homeValue - amtDown)/fifteenPctDivider;
        } else {
            pmiAmt = 0;
        }
        return new BigDecimal(pmiAmt).setScale(2,RoundingMode.HALF_EVEN).doubleValue();
    }

}
