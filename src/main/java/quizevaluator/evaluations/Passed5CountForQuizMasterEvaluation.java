package quizevaluator.evaluations;

public class Passed5CountForQuizMasterEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountQuizMaster(5);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed5CountForQuizMasterEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Bestanden gesamt";
    }

}
