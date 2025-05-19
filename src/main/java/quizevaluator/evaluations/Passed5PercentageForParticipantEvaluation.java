package quizevaluator.evaluations;

import quizevaluator.*;

public class Passed5PercentageForParticipantEvaluation implements Evaluation {

    public static double passedPercentage(
        final ResultsByQuizMasterAndParticipant results,
        final String name,
        final int excused
    ) {
        return Evaluation.passedPercentageParticipant(
            results,
            name,
            Passed5CountForParticipantEvaluation::passedCount,
            excused
        );
    }

    @Override
    public String cellText(final ResultsByQuizMasterAndParticipant results, final String name, final int excused) {
        return String.format(
            "%.2f",
            Passed5PercentageForParticipantEvaluation.passedPercentage(results, name, excused)
        );
    }

    @Override
    public Integer evaluation(final ResultsByQuizMasterAndParticipant results, final String name, final int excused) {
        return 0;
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
