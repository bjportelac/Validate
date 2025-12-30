package io.validate.sme.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.validate.sme.entity.*;
import io.validate.sme.repositories.EvaluationRepository;
import io.validate.sme.utils.Formulas;
import io.validate.sme.utils.Sorter;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class EvaluationServiceImpl implements EvaluationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationServiceImpl.class);
    private static final ObjectMapper MAPPER =  new ObjectMapper();

    private static final String EQUITIES = "PATRIMONIO";
    private static final String DEBT_CAPACITY = "CAPACIDAD_DE_ENDEUDAMIENTO";
    private static final String DEBT_RATIO = "RAZON_DE_ENDEUDAMIENTO";
    private static final String OPERATING_PROFIT = "UTILIDAD_OPERATIVA";
    private static final String OPERATING_MARGIN = "MARGEN_OPERATIVO";

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
    public Report getEvaluation(@Nonnull String id) {
        return repository.findById(id);
    }

    /**
     * @param data represents the financial data to be evaluated to be stored.
     * @return the report.
     */
    @NotNull
    @Override
    public Report createEvaluation(@Nonnull FinancialData data) {
        String reportId = UUID.randomUUID().toString();
        AnalysisResult result = performEvaluation(data);
        Report report = new Report();

        report.setId(reportId);
        report.setResult(result);
        report.setFinancialData(data);

        return repository.create(report);
    }

    private AnalysisResult performEvaluation(@Nonnull FinancialData data) {

        AnalysisResult result = new AnalysisResult();
        Double equities = Formulas.getEquities(data.getAssets(), data.getLiabilities());
        Double operatingProfit = Formulas.getOperatingProfit(data.getIncome(), data.getExpenses());

        Map<String,Double> numericalResults = Map.of(
                EQUITIES,  Formulas.getEquities(data.getAssets(), data.getLiabilities()),
                DEBT_CAPACITY, Formulas.getPaymentCapacity(data.getIncome(), data.getExpenses(), data.getLiabilities()),
                DEBT_RATIO, Formulas.getDebtRatio(data.getLiabilities(), equities),
                OPERATING_PROFIT, operatingProfit,
                OPERATING_MARGIN, Formulas.operatingMargin(operatingProfit,data.getIncome())
        );

        AIDecision decision = MAPPER.readValue(computeMessage(numericalResults, data), AIDecision.class);
        List<LoanOption> ranking = null;

        if(decision.id() == 0){
            ranking = Sorter.sortByRanking(data.getLoanOptions(), decision.ranking());
            result.setOption(ranking.removeFirst());
        } else {
            ranking = data.getLoanOptions();
            var selectedOption = ranking.remove(decision.id() - 1);
            result.setOption(selectedOption);
        }

        result.setIndicators(numericalResults);
        result.setMessage(decision.reason());
        result.setRanking(ranking);

        return result;
    }

    private String computeMessage(Map<String, Double> indicators, FinancialData data) {
        try (Client client = Client.builder().apiKey("").build()) {
            String message = """
                You are a financial decision engine.
                
                Your task is to evaluate loan options using ALL provided financial information and select the best option, or none.
                
                STRICT RULES (MANDATORY):
                - Perform all reasoning internally.
                - DO NOT reveal analysis, calculations, assumptions, or intermediate steps.
                - DO NOT restate, summarize, or explain the input data.
                - DO NOT include headings, lists, markdown, or code blocks.
                - DO NOT include any text outside the required output format.
                - The "reason" field MUST be written in Spanish.
                - Any deviation from the output format is an error.
                
                INPUT INTERPRETATION RULES (MANDATORY):
                - financial data contains pre-calculated financial indicators and MUST be considered.
                - assets, income, liabilities and expenses provide additional context and MUST influence the decision.
                - the monthlyPayments in liabilities SHOULD be considered to rule out an option.
                - averageIncome indicate monthly income .
                - fixedExpenses and varyingExpenses represent the expenses per month.
                - PAYMENT_CAPACITY indicate remaining liquidity per month.
                - assetsValue supports debt capacity but does NOT override payment sustainability.
                
                DECISION RULES:
                - Monthly payment must be sustainable considering:
                  PAYMENT_CAPACITY, monthlyPayments from liabilities and the quota per loan option.
                - The paymentTerm represents the amount of monthly payment the user will do.
                - Prefer lower effective cost of credit.
                - Loan amount must be coherent with income, assets, and remaining liquidity.
                
                OUTPUT FORMAT (EXACT, NO EXTRA TEXT):
                {
                  "id": <integer between 1 and n>,
                  "reason": "<max 5 sentences>",
                  "ranking": [<list of option ids ordered from best to worst>]
                }
                
                If NO option is financially viable or responsible, return:
                {
                  "id": 0,
                  "reason": "<max 5 sentences>",
                  "ranking": [<list of option ids ordered from best to worst>]
                }
                
                INPUT:
                - indicators: %s
                - loanOptions: %s
                - assets: %s
                - income: %s
                - liabilities: %s
                - expenses: %s
                """.formatted(
                    indicators.toString(),
                    data.getLoanOptions().toString(),
                    data.getAssets().toString(),
                    data.getIncome().toString(),
                    data.getLiabilities().toString(),
                    data.getExpenses().toString()
            );


            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-lite", message, null);
            LOGGER.info("Completed AI prompt and response.");

            return response.text();
        }
    }

}
