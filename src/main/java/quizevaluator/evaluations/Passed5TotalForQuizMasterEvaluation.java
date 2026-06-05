package quizevaluator.evaluations;

public class Passed5TotalForQuizMasterEvaluation implements IntegerEvaluation {

    @Override
    public Integer apply(final ResultData data) {
        if (Passed5CountForQuizMasterEvaluation.passedCount(data) == 0) {
            return 0;
        }
        final int passedPercentage = (int)Passed5PercentageForQuizMasterEvaluation.passedPercentage(data);
        return Math.min(10, (passedPercentage / 10) + 1);
    }

    @Override
    public String title() {
        return "Bestanden Bewertung";
    }

}
