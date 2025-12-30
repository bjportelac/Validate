package io.validate.sme.entity;

/**
 * Represents a Liabilities summary.
 *
 * @param debtAmount Is the amount of money in debt, 0.0 if user doesn't have debt.
 * @param monthlyPayments Is the amount of money the user pays monthly to cover the debts.
 */
public record Liabilities(
        double debtAmount,
        double monthlyPayments) {

    @Override
    public String toString() {
        return "Liabilities: {debtAmount=" + debtAmount + ", monthlyPayments=" + monthlyPayments + '}';
    }
}
