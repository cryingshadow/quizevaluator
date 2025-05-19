package quizevaluator.evaluations;

public class Passed5PercentageForParticipantEvaluation implements Evaluation {

    public static double passedPercentage(final ResultData data) {
        return Evaluation.passedPercentageParticipant(data, Passed5CountForParticipantEvaluation::passedCount);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", Passed5PercentageForParticipantEvaluation.passedPercentage(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
