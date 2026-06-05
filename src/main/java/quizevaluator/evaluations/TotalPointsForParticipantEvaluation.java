package quizevaluator.evaluations;

public class TotalPointsForParticipantEvaluation implements IntegerEvaluation {

    @Override
    public Integer apply(final ResultData data) {
        return data.totalPointsAsParticipant();
    }

    @Override
    public String title() {
        return "Punkte gesamt";
    }

}
