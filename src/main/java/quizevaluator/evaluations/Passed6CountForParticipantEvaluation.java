package quizevaluator.evaluations;

public class Passed6CountForParticipantEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountParticipant(6);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed6CountForParticipantEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Bestanden gesamt";
    }

}
