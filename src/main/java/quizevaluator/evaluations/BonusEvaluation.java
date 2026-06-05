package quizevaluator.evaluations;

import java.util.stream.*;

public interface BonusEvaluation extends IntegerEvaluation {

    @Override
    default public Integer apply(final ResultData data) {
        return Stream.of(this.bonus1(), this.bonus2(), this.bonus3()).mapToInt(c -> c.apply(data)).sum();
    }

    BonusCalculation bonus1();

    BonusCalculation bonus2();

    BonusCalculation bonus3();

}
