package quizevaluator.evaluations;

public class Passed8PercentageForParticipantEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageParticipant(Passed8CountForParticipantEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed8PercentageForParticipantEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
