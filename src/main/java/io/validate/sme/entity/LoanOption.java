package io.validate.sme.entity;

/**
 * Represents a loan option offered by an organization.
 *
 * @param id                        the numerical identifier of the selected option
 * @param financialInstitutionType  the type of financial organization offering the loan
 * @param offeredLoanValue          the value of the loan offered
 * @param paymentTerm               the payment term in months
 * @param quota                     the monthly payment amount
 * @param interestRate              the interest rate applied
 * @param interestRateType          the type of interest rate
 */
public record LoanOption(
        int id,
        String financialInstitutionType,
        double offeredLoanValue,
        int paymentTerm,
        double quota,
        double interestRate,
        String interestRateType) {

    @Override
    public String toString() {
        return "LoanOption{" +
                "id=" + id +
                ", financialInstitutionType='" + financialInstitutionType + '\'' +
                ", offeredLoanValue=" + offeredLoanValue +
                ", paymentTerm=" + paymentTerm +
                ", quota=" + quota +
                ", interestRate=" + interestRate +
                ", interestRateType='" + interestRateType + '\'' +
                '}';
    }

}