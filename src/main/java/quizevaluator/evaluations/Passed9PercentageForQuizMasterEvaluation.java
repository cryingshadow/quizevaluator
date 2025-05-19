package quizevaluator.evaluations;

public class Passed9PercentageForQuizMasterEvaluation implements Evaluation {

    public static double passedPercentage(final ResultData data) {
        return Evaluation.passedPercentageQuizMaster(data, Passed9CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.format("%.2f", Passed9PercentageForQuizMasterEvaluation.passedPercentage(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
