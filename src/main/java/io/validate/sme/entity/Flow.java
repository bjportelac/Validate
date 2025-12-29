package io.validate.sme.entity;

/**
 * Represents a flow with a flag indicating if money is left and the remaining amount.
 *
 * @param isMoneyLeft  true if there is money left, false otherwise
 * @param amountLeft   the amount of money left
 */
public record Flow(boolean isMoneyLeft, double amountLeft) {
}
