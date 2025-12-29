package io.validate.sme.entity;

/**
 * Represents a Liabilities summary.
 *
 * @param hasDebt Represents if the user has debts.
 * @param debtAmount Is the amount of money in debt, 0.0 if user doesn't have debt.
 * @param paymentsValue Is the amount of money the user pays monthly to cover the debts.
 * @param hasDelayInPayments Represents if the user reports delay in his payments.
 * @param delayInPayments represents the delay amount.
 */
public record Liabilities(
        boolean hasDebt,
        double debtAmount,
        double paymentsValue,
        boolean hasDelayInPayments,
        int delayInPayments) {
}
