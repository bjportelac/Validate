package io.validate.sme.service;

import io.validate.sme.entity.FinancialData;
import io.validate.sme.entity.Report;
import jakarta.annotation.Nonnull;

public interface EvaluationService {

    @Nonnull Report getEvaluation(@Nonnull String id);

    @Nonnull Report createEvaluation(@Nonnull FinancialData data);
}
