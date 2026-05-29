package quizevaluator.evaluations;

public class Passed9PercentageForParticipantEvaluation implements Evaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageParticipant(Passed9CountForParticipantEvaluation::passedCount);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", Passed9PercentageForParticipantEvaluation.passedPercentage(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
