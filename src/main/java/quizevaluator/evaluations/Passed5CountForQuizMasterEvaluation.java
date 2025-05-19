package quizevaluator.evaluations;

public class Passed5CountForQuizMasterEvaluation implements Evaluation {

    public static int passedCount(final ResultData data) {
        return Evaluation.passedCountQuizMaster(data, 5);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(Passed5CountForQuizMasterEvaluation.passedCount(data));
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
