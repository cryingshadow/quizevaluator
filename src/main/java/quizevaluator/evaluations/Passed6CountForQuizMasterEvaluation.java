package quizevaluator.evaluations;

public class Passed6CountForQuizMasterEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountQuizMaster(6);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed6CountForQuizMasterEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Bestanden gesamt";
    }

}
