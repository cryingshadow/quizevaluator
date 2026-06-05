package quizevaluator.evaluations;

public class Passed6TotalForQuizMasterEvaluation implements IntegerEvaluation {

    @Override
    public Integer apply(final ResultData data) {
        if (Passed6CountForQuizMasterEvaluation.passedCount(data) == 0) {
            return 0;
        }
        final int passedPercentage = (int)Passed6PercentageForQuizMasterEvaluation.passedPercentage(data);
        return Math.min(10, (passedPercentage / 10) + 1);
    }

    @Override
    public String title() {
        return "Bestanden Bewertung";
    }

}
