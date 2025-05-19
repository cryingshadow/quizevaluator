package quizevaluator.evaluations;

public class Passed6CountForQuizMasterEvaluation implements Evaluation {

    public static int passedCount(final ResultData data) {
        return Evaluation.passedCountQuizMaster(data, 6);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(Passed6CountForQuizMasterEvaluation.passedCount(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return 0;
    }

    @Override
    public String title() {
        return "Bestanden gesamt";
    }

}
