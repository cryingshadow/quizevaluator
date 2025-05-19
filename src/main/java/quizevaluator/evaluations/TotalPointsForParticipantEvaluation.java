package quizevaluator.evaluations;

public class TotalPointsForParticipantEvaluation implements Evaluation {

    public static int totalPoints(final ResultData data) {
        return data.results().values().stream().mapToInt(map -> map.getOrDefault(data.name(), 0).intValue()).sum();
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(TotalPointsForParticipantEvaluation.totalPoints(data));
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
