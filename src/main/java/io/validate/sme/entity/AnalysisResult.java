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

    public Map<String, Double> getIndicators() {
        return indicators;
    }

    public void setIndicators(Map<String, Double> indicators) {
        this.indicators = indicators;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoanOption getOption() {
        return option;
    }

    public void setOption(LoanOption option) {
        this.option = option;
    }
}
