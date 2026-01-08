package io.validate.sme.controllers;

import com.couchbase.client.core.error.DocumentNotFoundException;
import io.validate.sme.entity.FinancialData;
import io.validate.sme.entity.Report;
import io.validate.sme.service.EvaluationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validate/api/v1")
@CrossOrigin(origins = "*")
public class EvaluationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationController.class);

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    private static final String DOCUMENT_NOT_FOUND = "Document Not Found";

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable String id) {
        try {
            var evaluation = evaluationService.getEvaluation(id);
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } catch (DocumentNotFoundException e) {
            LOGGER.error("Error {} Id: {}", DOCUMENT_NOT_FOUND, id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            LOGGER.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Report> createReport(@RequestBody FinancialData evaluation) {
        String id = null;
        try {
            var newEvaluation = evaluationService.createEvaluation(evaluation);
            id = newEvaluation.getId();

            return new ResponseEntity<>(newEvaluation, HttpStatus.CREATED);
        } catch (DocumentNotFoundException e) {
            LOGGER.error("Error: Document already exists: {}", id);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            LOGGER.error(INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
