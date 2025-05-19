package quizevaluator.evaluations;

public class Passed5PercentageForQuizMasterEvaluation implements Evaluation {

    public static double passedPercentage(final ResultData data) {
        return Evaluation.passedPercentageQuizMaster(data, Passed5CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", Passed5PercentageForQuizMasterEvaluation.passedPercentage(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
