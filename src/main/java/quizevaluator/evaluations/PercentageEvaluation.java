package quizevaluator.evaluations;

import java.util.function.*;

public interface PercentageEvaluation extends Evaluation, Function<ResultData, Double> {

    @Override
    default String cellText(final ResultData data) {
        return String.format("%.2f", this.apply(data));
    }

}
