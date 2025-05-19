package quizevaluator.evaluations;

public class Passed9CountForParticipantEvaluation implements Evaluation {

    public static int passedCount(final ResultData data) {
        return Evaluation.passedCountParticipant(data, 9);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(Passed9CountForParticipantEvaluation.passedCount(data));
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
