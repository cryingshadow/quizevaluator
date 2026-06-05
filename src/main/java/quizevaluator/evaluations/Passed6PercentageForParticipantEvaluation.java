package quizevaluator.evaluations;

public class Passed6PercentageForParticipantEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageParticipant(Passed6CountForParticipantEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed6PercentageForParticipantEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
