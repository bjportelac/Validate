package io.validate.sme.repositories;

import io.validate.sme.entity.Report;

public interface EvaluationRepository {

    Report create(Report report);

    Report findById(String id);
}
