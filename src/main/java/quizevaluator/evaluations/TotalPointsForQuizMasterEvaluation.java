package quizevaluator.evaluations;

public class TotalPointsForQuizMasterEvaluation implements IntegerEvaluation {

    @Override
    public Integer apply(final ResultData data) {
        return data.totalPointsInOwnQuiz();
    }

    @Override
    public String title() {
        return "Punkte gesamt";
    }

}
