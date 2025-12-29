package io.validate.sme.repositories;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import io.validate.sme.entity.Report;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

@Repository
public class EvaluationRepositoryImpl implements EvaluationRepository{

    private final Collection CBCollection;

    public EvaluationRepositoryImpl(Bucket bucket) {
        this.CBCollection = bucket.scope("ValidateApp").collection("ValidateEvaluations");
    }

    /**
     * @param report represents the report to be stored.
     * @return the report.
     */
    @Override
    public Report create(@Nonnull Report report) {
        CBCollection.insert(report.getId(), report);
        return report;
    }

    /**
     * @param id is the unique identifier for the report.
     * @return the report if it is found.
     */
    @Override
    public Report findById(@Nonnull String id) {
        return CBCollection.get(id).contentAs(Report.class);
    }
}
