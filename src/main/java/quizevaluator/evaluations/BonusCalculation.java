package quizevaluator.evaluations;

import java.util.function.*;

public record BonusCalculation(
    Function<ResultData, Integer> calculation,
    int threshold
) implements Function<ResultData, Integer> {

    @Override
    public Integer apply(final ResultData data) {
        return this.calculation().apply(data) >= this.threshold() ? 1 : 0;
    }

}
