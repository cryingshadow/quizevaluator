package quizevaluator.evaluations;

public class Passed6PercentageForParticipantEvaluation implements Evaluation {

    public static double passedPercentage(final ResultData data) {
        return Evaluation.passedPercentageParticipant(data, Passed6CountForParticipantEvaluation::passedCount);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", Passed6PercentageForParticipantEvaluation.passedPercentage(data));
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
