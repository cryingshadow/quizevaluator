package quizevaluator.evaluations;

public class Passed8PercentageForQuizMasterEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageQuizMaster(Passed8CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed8PercentageForQuizMasterEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Gut bestanden Prozent";
    }

}
