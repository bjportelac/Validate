package io.validate.sme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FinancialData implements Serializable {

    private String companyName;
    private String companyMainActivity;
    private int operationalTime;
    private int employeeAmount;

    private Assets assets;
    private Income income;
    private Expenses expenses;
    private Liabilities liabilities;

    private List<LoanOption> loanOptions;

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyMainActivity() {
        return companyMainActivity;
    }

    public int getOperationalTime() {
        return operationalTime;
    }

    public int getEmployeeAmount() {
        return employeeAmount;
    }

    public Assets getAssets() {
        return assets;
    }

    public Income getIncome() {
        return income;
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public Liabilities getLiabilities() {
        return liabilities;
    }

    public List<LoanOption> getLoanOptions() {
        return loanOptions;
    }
}
