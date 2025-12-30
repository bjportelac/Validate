package io.validate.sme.entity;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class AnalysisResult {

    private Map<String, Double> indicators;
    private String message;
    private LoanOption option;
    private List<LoanOption> ranking;

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

    public List<LoanOption> getRanking() {
        return ranking;
    }

    public void setRanking(List<LoanOption> ranking) {
        this.ranking = ranking;
    }
}
