/*
 *  @(#)DefaultExtraPmt.java	1.0 2009/08/08
 *
 */
package jmortgage.payment.extra;

import jmortgage.amortization.PmtKey;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * The default implementatiion of <tt>ExtraPmt</tt>. This object contains the
 * amount of the extra payment, the number of payments to which the extra
 * payment will be applied, and a PmtKey object. The PmtKey object indicates the
 * start date of the extra payment. This start date must match exactly with the
 * date of one of the regular mortgage payments. The PmtKey also indicates the
 * interval of the extra payment. Valid interval values are Onetime, Yearly,
 * Monthly, Biweekly, and Weekly. This object is immutable as long as the
 * implementation of <tt>PmtKey</tt> it uses is immutable. <tt>DefaultPmtKey</tt>
 * is immutable, so if this instance uses an implemntation of
 * <tt>DefaultPmtKey</tt>, its thread safety is guaranteed.
 * @since 1.0
 * @author David Armstrong
 */
public final class DefaultExtraPmt implements ExtraPmt {

    private final PmtKey pmtKey;
    private final int count;
    private final double amount;

    /**
     * Creates an object with the specified <tt>PmtKey</tt> instance, count, and
     * amount.
     * @param pmtKey The <tt>PmtKey</tt> instance
     * @param count The count value
     * @param amount The amount value
     */
    public DefaultExtraPmt(PmtKey pmtKey, int count, double amount) {

        if(pmtKey == null) {
            throw new IllegalArgumentException("The PmtKey object must not be null.");
        }
        if(count < 1) {
            throw new IllegalArgumentException("the count must be greater than 0.");
        }
        if(amount <= 0.0) {
            throw new IllegalArgumentException("The amount must be greater than 0.");
        }
        
        this.pmtKey = pmtKey;
        this.count = count;
        this.amount = amount;
    }

    /**
     * Creates an object with the specified <tt>PmtKey</tt> instance, count, and
     * amount. The count and amount values are passed in as <tt>Integer</tt> and
     * <tt>Double</tt> values respectively. This constructor exists so clients
     * can instantiate this object by using
     * {@link jmortgage.JMortgageFactory#getExtraPmt(java.lang.Class, java.lang.Class[], java.lang.Object[])},
     * which uses reflection to build objects.
     * @param pmtKey The <tt>PmtKey</tt> instance
     * @param count The count value
     * @param amount The amount value
     */
    public DefaultExtraPmt(PmtKey pmtKey, Integer count, Double amount) {
        this(pmtKey, count.intValue(), amount.doubleValue());
    }

    /**
     * Gets the <tt>PmtKey</tt> object for this extra payment. The <tt>PmtKey</tt>
     * object indicates the start date of the extra payment and the interval
     * between extra payments.
     * @return <tt>PmtKey</tt> instantiation
     */
    public PmtKey getPmtKey() { return pmtKey; }

    /**
     * Gets the number of consecutive times to apply the extra payment.
     * Consecutive is defined by the interval value indicated in the <tt>PmtKey</tt>
     * object.
     * @return count value
     */
    public int getCount() { return count; }

    /**
     * Gets the amount of the extra payment.
     * @return amount value
     */
    public double getAmount() { return amount; }

    /**
     * Sets the <tt>PmtKey</tt> instance. This method returns a new instance of
     * <tt>DefaultExtraPmt</tt>. The object returned will use
     * the specified <tt>PmtKey</tt> instance and will have the same count and
     * amount values as the object on which <tt>setPmtKey</tt> was called.
     * @param pmtKey <tt>PmtKey</tt> instance
     * @return <tt>DefaultExtraPmt</tt> instantiation
     */
    public ExtraPmt setPmtKey(final PmtKey pmtKey) {  return new DefaultExtraPmt(pmtKey, this.count, this.amount); }

    /**
     * Sets the count. This method returns a new instance of <tt>DefaultExtraPmt</tt>.
     * The object returned will use the specified count and will have the same
     * <tt>ExtraPmt</tt> instance and amount values as the object on which
     * <tt>setCount</tt> was called.
     * @param count The count value
     * @return <tt>DefaultExtraPmt</tt> instantiation
     */
    public ExtraPmt setCount(int count) { return new DefaultExtraPmt(this.pmtKey, count, this.amount); }

    /**
     * Sets the amount. This method returns a new instance of
     * <tt>DefaultExtraPmt</tt>. The object returned will use the specified
     * amount and will have the same <tt>ExtraPmt</tt> instance and count values
     * as the object on which <tt>setAmount</tt> was called.
     * @param amount The amount value
     * @return <tt>DefaultExtraPmt</tt> instantiation
     */
    public ExtraPmt setAmount(double amount) { return new DefaultExtraPmt(this.pmtKey, this.count, amount); }

    /**
     * Returns a String representation of this object.
     * @return a String representation
     */
    @Override
    public String toString() {
        return(new ToStringBuilder(this)
                .append("pmtKey", pmtKey)
                .append("count", count)
                .append("amount", amount)
                .toString());
    }
}
