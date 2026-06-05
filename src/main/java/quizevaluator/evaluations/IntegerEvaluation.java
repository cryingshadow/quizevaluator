package quizevaluator.evaluations;

import java.util.function.*;

public interface IntegerEvaluation extends Evaluation, Function<ResultData, Integer> {

    @Override
    default String cellText(final ResultData data) {
        return String.valueOf(this.apply(data));
    }

}
