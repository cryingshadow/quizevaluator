package quizevaluator.evaluations;

public class PointsPercentageForParticipantEvaluation implements PercentageEvaluation {

    public static double pointsPercentage(final ResultData data) {
        final double sumTimes100 = data.totalPointsAsParticipant() * 100;
        final int total = data.totalNumberOfQuizzesAsParticipant() * 10;
        if (total == 0) {
            return 0.0;
        }
        return sumTimes100 / total;
    }

    @Override
    public Double apply(final ResultData data) {
        return PointsPercentageForParticipantEvaluation.pointsPercentage(data);
    }

    @Override
    public String title() {
        return "Punkte Prozent";
    }

}
