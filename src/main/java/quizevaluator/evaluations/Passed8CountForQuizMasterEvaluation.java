package quizevaluator.evaluations;

public class Passed8CountForQuizMasterEvaluation implements Evaluation {

    public static int passedCount(final ResultData data) {
        return Evaluation.passedCountQuizMaster(data, 8);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(Passed8CountForQuizMasterEvaluation.passedCount(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Gut bestanden gesamt";
    }

}
