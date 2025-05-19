package quizevaluator.evaluations;

public class TotalPointsForQuizMasterEvaluation implements Evaluation {

    public static int totalPoints(final ResultData data) {
        return data.results().get(data.name()).values().stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(TotalPointsForQuizMasterEvaluation.totalPoints(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Punkte gesamt";
    }

}
