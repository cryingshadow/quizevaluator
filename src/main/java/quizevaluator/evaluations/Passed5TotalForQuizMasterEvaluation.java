package quizevaluator.evaluations;

public class Passed5TotalForQuizMasterEvaluation implements Evaluation {

    private static int passedTotal(final ResultData data) {
        if (Passed5CountForQuizMasterEvaluation.passedCount(data) == 0) {
            return 0;
        }
        final int passedPercentage = (int)Passed5PercentageForQuizMasterEvaluation.passedPercentage(data);
        return Math.min(10, (passedPercentage / 10) + 1);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(Passed5TotalForQuizMasterEvaluation.passedTotal(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return Passed5TotalForQuizMasterEvaluation.passedTotal(data);
    }

    @Override
    public String title() {
        return "Bestanden Bewertung";
    }

}
