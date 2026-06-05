package quizevaluator.evaluations;

public class Passed5PercentageForParticipantEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageParticipant(Passed5CountForParticipantEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed5PercentageForParticipantEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
