/*
 *  @(#)JMortgageFactory.java	1.0 2009/08/06
 *
 */

package jmortgage;

import jmortgage.payment.PmtCalculator;
import jmortgage.payment.DefaultPmtCalculator;
import jmortgage.payment.extra.ExtraPmt;
import jmortgage.payment.extra.DefaultExtraPmt;
import jmortgage.payment.extra.ExtraPmtMapBuilder;
import jmortgage.payment.extra.DefaultExtraPmtMapBuilder;
import jmortgage.payment.Interval;
import jmortgage.amortization.FixedAmortizationBuilder;
import jmortgage.amortization.DefaultFixedAmortizationBuilder;
import jmortgage.amortization.PmtKey;
import jmortgage.amortization.DefaultPmtKey;
import org.apache.commons.collections.functors.InstantiateFactory;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;


/**
 * Factory class that builds implementations for all interfaces defined in
 * JMortgage. Uses reflection to build implementations. This ensures that
 * future implementations of the interfaces can be instantiated by this factory
 * class. Also, has convenience methods to implement instances of the default
 * implementations of the JMortgage interfaces. Uses Apache's common collections
 * object {@link InstantiateFactory} to create objects via reflection.
 *
 * @author David Armstrong
 *
 * @version 1.0
 */
public class JMortgageFactory {

    /**
     * Builds an implementation of PmtCalculator using reflection.
     * <p>To build {@link DefaultPmtCalculator} using this method:<pre>
     * PmtCalculator pmtCalculator = JMortgageFactory.getPmtCalculator(
     *  jmortgage.payment.DefaultPmtCalculator.class,
     *  new Class[] {Interval.class, Double.class, Double.class, Integer.class},
     *  new Object[] { Interval.Monthly, (double)100000, 5.75, 360 });</pre></p>
     * @param instance The class of the implementation of PmtCalculator to
     * instantiate.
     * @param classes The constructor parameter types. The array of classes must
     * be in the same order as the array of object arguments and they must equal
     * in number.
     * @param args The constructor arguments.
     * @return PmtCalculator instantiation
     */
    public static PmtCalculator getPmtCalculator(Class instance, Class[] classes, Object[] args) {

        return((PmtCalculator)InstantiateFactory.getInstance(
                instance, classes, args).create());
    }

    /**
     * Builds an implementation of PmtCalculator using reflection using its
     * empty constructor if it has one.
     * @param instance The class of the implementation of PmtCalculator to
     * instantiate.
     * @return PmtCalculator instantiation
     */
    public static PmtCalculator getPmtCalculator(Class instance) {
        return((PmtCalculator)new InstantiateFactory(instance).create());
    }

    /**
     * Convenience method for creating a {@link DefaultPmtCalculator}.
     * @param interval interval to be used to calculate the payment. Valid
     * values are Monthly, Biweekly, and Weekly.
     * @param loanAmt loan amount used to calculate the payment.
     * @param interestRate the interest rate used to calculate the payment.
     * @param years the number of years in the mortgage term.
     * @return PmtCalculator instantiation, in this case, DefaultPmtCalculator
     */
    public static PmtCalculator getDefaultPmtCalculator(Interval interval, double loanAmt, double interestRate, int years) {
        return(new DefaultPmtCalculator(interval, loanAmt, interestRate, years));
    }

    /**
     * Builds an implementation of FixedAmortizationBuilder using reflection.
     * @param instance  The class of the implementation of
     * FixedAmortizationBuilder to instantiate.
     * @param classes The constructor parameter types. The array of classes must
     * be in the same order as the array of object arguments and they must equal
     * in number.
     * @param args The constructor arguments.
     * @return FixedAmortizationBuilder instantiation
     */
    public static FixedAmortizationBuilder getFixedAmortizationBuilder(Class instance, Class[] classes, Object[] args) {

        return((FixedAmortizationBuilder)InstantiateFactory.getInstance(
                instance, classes, args).create());
    }

    /**
     * Builds an implementation of FixedAmortizationBuilder using reflection
     * using its empty constructor if it has one.
     * @param instance  The class of the implementation of
     * FixedAmortizationBuilder to instantiate.
     * @return FixedAmortizationBuilder instantiation
     */
    public static FixedAmortizationBuilder getFixedAmortizationBuilder(Class instance) {

        return((FixedAmortizationBuilder)new InstantiateFactory(instance).create());
    }

    /**
     * Convenience method for creating a {@link DefaultFixedAmortizationBuilder}
     * @param pmtCalc The PmtCalculator instance to be used to build the
     * amortization table.
     * @param pmtKey The PmtKey object to be used to build the amortization
     * table.
     * @return FixedAmortizationBuilder instantiation, in this case,
     * DefaultFixedAmortizationBuilder.
     */
    public static FixedAmortizationBuilder getDefaultFixedAmortizationBuilder(final PmtCalculator pmtCalc, final PmtKey pmtKey) {
        return(new DefaultFixedAmortizationBuilder(pmtCalc, pmtKey));
    }

    /**
     * Builds an implementation of ExtraPmt using reflection.
     * @param instance  The class of the implementation of ExtraPmt to
     * instantiate.
     * @param classes The constructor parameter types. The array of classes must
     * be in the same order as the array of object arguments and they must equal
     * in number.
     * @param args The constructor arguments.
     * @return ExtraPmt instantiation
     */
    public static ExtraPmt getExtraPmt(Class instance, Class[] classes, Object[] args) {

        return((ExtraPmt)InstantiateFactory.getInstance(
                instance, classes, args).create());
    }

    /**
     * Builds an implementation of ExtraPmt using reflection using its empty
     * constructor if it has one.
     * @param instance The class of the implementation of ExtraPmt to
     * instantiate.
     * @return ExtraPmt
     */
    public static ExtraPmt getExtraPmt(Class instance) {

        return((ExtraPmt)new InstantiateFactory(instance).create());
    }

    /**
     * Convenience method for creating a {@link DefaultExtraPmt}
     * @param pmtKey The PmtKey object to be used to build the DefaultExtraPmt.
     * The interval value in the PmtKey is the interval in which to apply the
     * extra payments. Valid values are Monthly, Biweekly, Weekly, Yearly, and
     * OneTime.
     * @param count The count of consecutive payments to which to apply the
     * extra payment.
     * @param amount The amount of the extra payment.
     * @return ExtraPmt instantiation, in this case, DefaultExtraPmt
     */
    public static ExtraPmt getDefaultExtraPmt(PmtKey pmtKey, int count, double amount) {
        return(new DefaultExtraPmt(pmtKey, count, amount));
    }

    /**
     * Builds an implementation of PmtKey using reflection.
     * @param instance  The class of the implementation of PmtKey to
     * instantiate.
     * @param classes The constructor parameter types. The array of classes must
     * be in the same order as the array of object arguments and they must equal
     * in number.
     * @param args The constructor arguments.
     * @return PmtKey instantiation
     */
    public static PmtKey getPmtKey(Class instance, Class[] classes, Object[] args) {

        return((PmtKey)InstantiateFactory.getInstance(
                instance, classes, args).create());
    }

    /**
     * Builds an implementation of PmtKey using reflection using its empty
     * constructor if it has one.
     * @param instance  The class of the implementation of PmtKey to
     * instantiate.
     * @return PmtKey instantiation
     */
    public static PmtKey getPmtKey(Class instance) {
        return((PmtKey)new InstantiateFactory(instance).create());
    }

    /**
     * Convenience method for creating a {@link DefaultPmtKey}
     * @param interval <tt>Interval</tt> enum that determines the length of time
     * between keys.
     * @return PmtKey instantiation, in this case, DefaultPmtKey.
     */
    public static PmtKey getDefaultPmtKey(final Interval interval) {
        return(new DefaultPmtKey(interval));
    }

    /**
     * Convenience method for creating a {@link DefaultPmtKey}
     * @param interval <tt>Interval</tt> enum that determines the length of time
     * between keys.
     * @param cal The Calendar object to be used to build the PmtKey object.
     * @return PmtKey instantiation, in this case, DefaultPmtKey.
     */
    public static PmtKey getDefaultPmtKey(final Interval interval, final Calendar cal) {
        return(new DefaultPmtKey(interval, cal));
    }

    /**
     * Convenience method for creating a {@link DefaultPmtKey}
     * @param interval <tt>Interval</tt> enum that determines the length of time
     * between keys.
     * @param date The Date object to be used to build the PmtKey object.
     * @return PmtKey instantiation, in this case, DefaultPmtKey
     */
    public static PmtKey getDefaultPmtKey(final Interval interval, final Date date) {
        return(new DefaultPmtKey(interval, date));
    }

    /**
     * Convenience method for creating a {@link DefaultPmtKey}
     * @param interval <tt>Interval</tt> enum that determines the length of time
     * between keys.
     * @param dateStr The date String object to be used to build the PmtKey
     * object.
     * @param pattern The pattern of the date string object.
     * @return PmtKey instantiation, in this case, DefaultPmtKey
     * @throws ParseException
     */
    public static PmtKey getDefaultPmtKey(final Interval interval, final String dateStr, final String pattern) throws ParseException {
        return(new DefaultPmtKey(interval, dateStr, pattern));
    }

    /**
     * Builds an implementation of ExtraPmtMapBuilder using reflection.
     * @param instance  The class of the implementation of PmtKey to
     * instantiate.
     * @param classes The constructor parameter types. The array of classes must
     * be in the same order as the array of object arguments and they must equal
     * in number.
     * @param args The constructor arguments.
     * @return ExtraPmtMapBuilder instantiation
     */
    public static ExtraPmtMapBuilder getExtraPmtMapBuilder(Class instance, Class[] classes, Object[] args) {

        return((ExtraPmtMapBuilder)InstantiateFactory.getInstance(
                instance, classes, args).create());
    }

    /**
     * Builds an implementation of ExtraPmtMapBuilder using reflection using
     * its empty constructor if it has one.
     * @param instance  The class of the implementation of PmtKey to
     * instantiate.
     * @return ExtraPmtMapBuilder instantiation
     */
    public static ExtraPmtMapBuilder getExtraPmtMapBuilder(Class instance) {

        return((ExtraPmtMapBuilder)new InstantiateFactory(instance).create());
    }

    /**
     * Convenience method for creating a {@link DefaultExtraPmtMapBuilder}.
     * An Interval object is not passed in and defaults to Monthly.
     * @param pmtKey The pmtKey to be used to create the ExtraPmtMapBuilder
     * @param years The years in the mortgage term for which to build the
     * ExtraPmtMapBuilder
     * @return ExtraPmtMapBuilder instantiation, in this case,
     * DefaultExtraPmtMapBuilder
     */
    public static ExtraPmtMapBuilder getDefaultExtraPmtMapBuilder(final PmtKey pmtKey, int years) {
        return(new DefaultExtraPmtMapBuilder(pmtKey, years));
    }

}
