package quizevaluator.evaluations;

import java.util.function.*;

public record Evaluations(
    QuizMasterEvaluations quizMasterEvaluations,
    ParticipantEvaluations participantEvaluations
) implements Function<ResultData, Integer> {

    @Override
    public Integer apply(final ResultData data) {
        return this.quizMasterEvaluations().apply(data) + this.participantEvaluations().apply(data);
    }

}
