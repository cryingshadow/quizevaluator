package quizevaluator.evaluations;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public record BonusCalculation(
    Function<ResultData, Integer> calculation,
    int threshold
) {

    public static int bonus(
        final ResultData data,
        final BonusCalculation... calculations
    ) {
        return BonusCalculation.bonus(data, Arrays.stream(calculations));
    }

    public static int bonus(
        final ResultData data,
        final Collection<BonusCalculation> calculations
    ) {
        return BonusCalculation.bonus(data, calculations.stream());
    }

    public static int bonus(
        final ResultData data,
        final Stream<BonusCalculation> calculations
    ) {
        return
            calculations
            .mapToInt(c -> c.calculation().apply(data) >= c.threshold() ? 1 : 0)
            .sum();
    }

}
