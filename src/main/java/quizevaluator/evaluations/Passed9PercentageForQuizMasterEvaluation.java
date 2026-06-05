package quizevaluator.evaluations;

public class Passed9PercentageForQuizMasterEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageQuizMaster(Passed9CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed9PercentageForQuizMasterEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
