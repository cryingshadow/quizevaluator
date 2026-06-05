package quizevaluator.evaluations;

public class PointsPercentageForQuizMasterEvaluation implements PercentageEvaluation {

    public static double pointsPercentage(final ResultData data) {
        final double sumTimes100 = data.totalPointsInOwnQuiz() * 100;
        final int total = data.totalNumberOfParticipantsInOwnQuiz() * 10;
        if (total == 0) {
            return 0.0;
        }
        return sumTimes100 / total;
    }

    @Override
    public Double apply(final ResultData data) {
        return PointsPercentageForQuizMasterEvaluation.pointsPercentage(data);
    }

    @Override
    public String title() {
        return "Punkte Prozent";
    }

}
