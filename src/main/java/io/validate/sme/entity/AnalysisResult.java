package io.validate.sme.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnalysisResult {

    private Map<String, Double> indicators;
    private String message;
    private LoanOption option;
}
