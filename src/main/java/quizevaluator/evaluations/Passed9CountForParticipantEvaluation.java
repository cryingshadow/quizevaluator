package quizevaluator.evaluations;

public class Passed9CountForParticipantEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountParticipant(9);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed9CountForParticipantEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Gut bestanden gesamt";
    }

}
