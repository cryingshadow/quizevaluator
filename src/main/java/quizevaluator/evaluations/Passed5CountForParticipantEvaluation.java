package quizevaluator.evaluations;

public class Passed5CountForParticipantEvaluation implements IntegerEvaluation {

    public static int passedCount(final ResultData data) {
        return data.passedCountParticipant(5);
    }

    @Override
    public Integer apply(final ResultData data) {
        return Passed5CountForParticipantEvaluation.passedCount(data);
    }

    @Override
    public String title() {
        return "Bestanden gesamt";
    }

}
