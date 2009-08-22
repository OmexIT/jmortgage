/*
 *  @(#)PMICalculator.java	1.2 2009/08/20
 *
 */

package jmortgage.pmi;

/**
 * A Private Mortgage Insurance(PMI) calculator. An implementation of this
 * interface calculates the PMI for a mortgage. PMI has to be paid for mortgages
 * in which the down payment is less than 20 percent of the home's value. PMI
 * typically has to be paid until the borrower has paid at least 22% of the
 * value of the home.
 *
 * @since 1.2
 * @author David Armstrong
 */
public interface PMICalculator {

    /**
     * Calculates the PMI for a mortgage based on the home's value and the loan
     * amount
     * @param homeValue the value of the home
     * @param loanAmt the loan amount If less than 20% of the home value, a
     * monthly PMI amount must be paid.
     * @return PMI amount
     */
    public double calcPMI(double homeValue, double loanAmt);

}
