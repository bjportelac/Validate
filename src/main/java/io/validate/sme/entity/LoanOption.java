package io.validate.sme.entity;

/**
 * Represents a loan option offered by an organization.
 *
 * @param financialInstitutionType  the type of financial organization offering the loan
 * @param offeredLoanValue          the value of the loan offered
 * @param paymentTerm               the payment term in months
 * @param monthlyPayment            the monthly payment amount
 * @param interestRate              the interest rate applied
 * @param interestRateType          the type of interest rate
 */
public record LoanOption(
        String financialInstitutionType,
        double offeredLoanValue,
        int paymentTerm,
        double monthlyPayment,
        double interestRate,
        String interestRateType) {

    @Override
    public String toString() {
        return "LoanOption: {" +
                "financialInstitutionType='" + financialInstitutionType + '\'' +
                ", offeredLoanValue=" + offeredLoanValue +
                ", paymentTerm=" + paymentTerm +
                ", monthlyPayment=" + monthlyPayment +
                ", interestRate=" + interestRate +
                ", interestRateType='" + interestRateType + '\'' +
                '}';
    }
}