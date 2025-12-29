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
    private Flow flow;

    private List<LoanOption> loanOptions;
}
