package quizevaluator.evaluations;

public class TotalPointsForQuizMasterEvaluation implements Evaluation {

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(data.totalPointsInOwnQuiz());
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
