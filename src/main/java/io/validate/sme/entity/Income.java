package io.validate.sme.entity;

/**
 * Represents an income item with an average income value.
 *
 * @param averageIncome Average income amount.
 */
public record Income(double averageIncome) {
    @Override
    public String toString() {
        return "Income: { averageIncome=" + averageIncome + '}';
    }
}

