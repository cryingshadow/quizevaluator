package quizevaluator.evaluations;

public class Passed9CountForQuizMasterEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountQuizMaster(9);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed9CountForQuizMasterEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Gut bestanden gesamt";
    }

}
