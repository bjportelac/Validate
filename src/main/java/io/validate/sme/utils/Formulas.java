package io.validate.sme.utils;


import io.validate.sme.entity.Assets;
import io.validate.sme.entity.Expenses;
import io.validate.sme.entity.Income;
import io.validate.sme.entity.Liabilities;

public final class Formulas {

    private Formulas() {
        throw new RuntimeException("Utility Classes should not be instantiated");
    }

    public static Double getEquities(Assets assets, Liabilities liabilities){
        return assets.assetsValue() - liabilities.debtAmount();
    }

    public static Double getPaymentCapacity(Income income, Expenses expenses, Liabilities liabilities){
        return income.averageIncome() - (expenses.fixedExpenses() + expenses.varyingExpenses() + liabilities.monthlyPayments());
    }

    public static Double getDebtRatio(Liabilities liabilities, Double equities) {
        if (equities <= 0.0) {
            return 1.0;
        }
        return liabilities.debtAmount() / equities;
    }

    public static Double getDebtCoverageRatio(Double paymentCapacity, Double loanPayment) {
        return paymentCapacity / loanPayment;
    }

    public static Double getOperatingProfit(Income income, Expenses expenses) {
        return income.averageIncome() - expenses.fixedExpenses() + expenses.varyingExpenses();
    }

    public static Double operatingMargin (Double operativeUtility, Income income) {
        return operativeUtility/income.averageIncome();
    }

}
