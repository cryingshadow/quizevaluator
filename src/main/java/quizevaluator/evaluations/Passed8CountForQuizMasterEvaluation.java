package quizevaluator.evaluations;

public class Passed8CountForQuizMasterEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountQuizMaster(8);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed8CountForQuizMasterEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Gut bestanden gesamt";
    }

}
