package io.validate.sme.entity;

/**
 * Represents an income item with a stable income description and an average income value.
 *
 * @param stableIncome Description of the stable income source.
 * @param averageIncome Average income amount.
 */
public record Income(String stableIncome, double averageIncome) {
}

