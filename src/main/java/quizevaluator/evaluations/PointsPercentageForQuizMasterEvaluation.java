package quizevaluator.evaluations;

public class PointsPercentageForQuizMasterEvaluation implements Evaluation {

    public static double pointsPercentage(final ResultData data) {
        final double sumTimes100 = TotalPointsForQuizMasterEvaluation.totalPoints(data) * 100;
        final int total = data.results().get(data.name()).size() * 10;
        return sumTimes100 / total;
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", PointsPercentageForQuizMasterEvaluation.pointsPercentage(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Punkte Prozent";
    }

}
