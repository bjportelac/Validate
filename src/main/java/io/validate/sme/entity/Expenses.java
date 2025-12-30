package io.validate.sme.entity;

/**
 * Represents the expenses of an entity, divided into fixed and varying expenses.
 *
 * @param fixedExpenses   the fixed expenses amount
 * @param varyingExpenses the varying expenses amount
 */
public record Expenses(double fixedExpenses, double varyingExpenses) {

    @Override
    public String toString() {
        return "Expenses: { fixedExpenses=" + fixedExpenses + ", varyingExpenses=" + varyingExpenses + '}';
    }
}
