package io.validate.sme.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.validate.sme.entity.AnalysisResult;
import io.validate.sme.entity.FinancialData;
import io.validate.sme.entity.LoanOption;
import io.validate.sme.entity.Report;
import io.validate.sme.repositories.EvaluationRepository;
import io.validate.sme.utils.Formulas;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EvaluationServiceImpl implements EvaluationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationServiceImpl.class);

    private static final String EQUITIES = "PATRIMONIO";
    private static final String DEBT_CAPACITY = "CAPACIDAD_DE_ENDUDAMIENTO";
    private static final String DEBT_RATIO = "RAZON_DE_ENDEUDAMIENTO";
    private static final String OPERATING_PROFIT = "UTILIDAD_OPERATIVA";
    private static final String PAYMENT_CAPACITY = "CAPACIDAD_DE_PAGO";

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
        StringBuilder builder =  new StringBuilder();

        Double debtCapacity = Formulas.getPaymentCapacity(data.getIncome(), data.getExpenses(), data.getLiabilities());
        Double equities = Formulas.getEquities(data.getAssets(), data.getLiabilities());
        double debtRatio = Formulas.getDebtRatio(data.getLiabilities(), equities);
        Double operatingProfit = Formulas.getOperatingProfit(data.getIncome(), data.getExpenses());
        Double paymentCapacity = Formulas.getPaymentCapacity(data.getIncome(), data.getExpenses(), data.getLiabilities());

        Map<String,Double> numericalResults = Map.of(
                EQUITIES, equities,
                DEBT_CAPACITY, debtCapacity,
                DEBT_RATIO, debtRatio,
                OPERATING_PROFIT, operatingProfit,
                PAYMENT_CAPACITY, paymentCapacity
        );

        result.setIndicators(numericalResults);

        if (debtRatio < 0.3) {
            builder.append("La razon de endeudamiento equivale a %.2f, solicitar un prestamo es viable.\n".formatted(debtCapacity));
        } else if (debtRatio >= 0.3 && debtRatio < 0.6) {
            builder.append("La razon de endeudamiento equivale a %.2f, solicitar un prestamo es viable pero se debe actuar con moderacion, se evaluaran las opciones.\n".formatted(debtCapacity));
        } else {
            builder.append("La razon de endeudamiento es superior a 0.6, NO es recomendable solicitar un préstamo en el momento.\n");
        }

        if (operatingProfit <= 0.09) {
            builder.append("El margen de ganacias ( %.2f ) no es suficiente para solicitar un préstamo;\n".formatted(operatingProfit));
        } else if (operatingProfit >= 0.1 && operatingProfit <=0.14) {
            builder.append("El margen de ganacias ( %.2f ) es aceptable, solicitar un prestamo es viable con estas ganancias pero se debe actuar con moderacion.\n".formatted(operatingProfit));
        } else if (operatingProfit >= 0.15 && operatingProfit <= 0.19) {
            builder.append("El margen de ganacias ( %.2f ) es bueno, solicitar un prestamo es viable con estas ganancias.\n".formatted(operatingProfit));
        } else {
            builder.append("El margen de ganacias ( %.2f ) es excelente, solicitar un prestamo es recomendable con estas ganancias.\n".formatted(operatingProfit));
        }

        List<LoanOption> options = new ArrayList<>();
        for (LoanOption option : data.getLoanOptions()) {
            //Discard by coverage ratio
            var coverageRatio = Formulas.getDebtCoverageRatio(paymentCapacity, option.monthlyPayment());
            if (coverageRatio < 1) {
                continue;
            }

            if (paymentCapacity - option.monthlyPayment() <= 0.0) {
                continue;
            }

            options.add(option);
        }

        if(options.isEmpty()) {
            builder.append("No hay una opcion disponible para el prestamo entre las opciones que el cliente ha dado. Por favor validar los indicadores numericos para encontrar una opcion");
            result.setOption(null);
            result.setMessage(builder.toString());

            return result;
        }

        Optional<LoanOption> bestOption = options.stream().max(Comparator.comparingDouble(LoanOption::offeredLoanValue));
        bestOption.ifPresent(result::setOption);

        result.setMessage(computeMessage(builder.toString(), result.getOption()));

        return result;
    }

    private String computeMessage(String context, LoanOption option) {
        try (Client client = Client.builder().apiKey("").build()) {
            String message = """
                    Hola, basado en los siguientes indicadores: %s \n
                    y la siguiente opcion de prestamo: %s \n
                    genera un mensaje de decision para el usuario, que sea corto y conciso.
                    """.formatted(context, option.toString());

            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash-lite", message, null);

            if(!Objects.requireNonNull(response.text()).isEmpty()){
                return response.text();
            } else {
                return "Basado en los indicadores: %s\n la opcion seleccionada es: %s".formatted(context, option.toString());
            }
        }
    }

}
