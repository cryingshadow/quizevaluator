package quizevaluator.evaluations;

public class Passed6PercentageForQuizMasterEvaluation implements PercentageEvaluation {

    public static double passedPercentage(final ResultData data) {
        return data.passedPercentageQuizMaster(Passed6CountForQuizMasterEvaluation::passedCount);
    }

    @Override
    public Double apply(final ResultData data) {
        return Passed6PercentageForQuizMasterEvaluation.passedPercentage(data);
    }

    @Override
    public String title() {
        return "Bestanden Prozent";
    }

}
