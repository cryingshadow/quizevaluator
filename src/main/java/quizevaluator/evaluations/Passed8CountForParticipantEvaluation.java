package quizevaluator.evaluations;

public class Passed8CountForParticipantEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountParticipant(8);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed8CountForParticipantEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Gut bestanden gesamt";
    }

}
