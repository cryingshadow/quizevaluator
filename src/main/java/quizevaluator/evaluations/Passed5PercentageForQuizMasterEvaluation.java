package quizevaluator.evaluations;

public class Passed5PercentageForQuizMasterEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageQuizMaster(Passed5CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed5PercentageForQuizMasterEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
