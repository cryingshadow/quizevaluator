package quizevaluator.evaluations;

public class Passed9PercentageForParticipantEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageParticipant(Passed9CountForParticipantEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed9PercentageForParticipantEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
