package io.validate.sme.service;

import io.validate.sme.entity.AnalysisResult;
import io.validate.sme.entity.FinancialData;
import io.validate.sme.entity.Report;
import io.validate.sme.repositories.EvaluationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EvaluationServiceImpl implements EvaluationService{

    private final EvaluationRepository repository;

    public EvaluationServiceImpl(EvaluationRepository repository) {
        this.repository = repository;
    }

    /**
     * @param id is the unique identifier for the report.
     * @return the report if it is found.
     */
    @NotNull
    @Override
    public Report getEvaluation(@NotNull String id) {
        return repository.findById(id);
    }

    /**
     * @param data represents the financial data to be evaluated to be stored.
     * @return the report.
     */
    @NotNull
    @Override
    public Report createEvaluation(@NotNull FinancialData data) {
        String reportId = UUID.randomUUID().toString();
        AnalysisResult result = new AnalysisResult();
        Report report = new Report();
        report.setId(reportId);
        report.setResult(result);
        report.setFinancialData(data);

        return repository.create(report);
    }
}
