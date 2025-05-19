package quizevaluator.evaluations;

public class BonusForParticipantEvaluation implements Evaluation {

    private static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                data -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(data),
                50
            ),
            new BonusCalculation(
                data -> (int)Passed5PercentageForParticipantEvaluation.passedPercentage(data),
                100
            ),
            new BonusCalculation(
                data -> (int)Passed8PercentageForParticipantEvaluation.passedPercentage(data),
                80
            )
        };

    private static int bonus(final ResultData data) {
        return BonusCalculation.bonus(data, BonusForParticipantEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(BonusForParticipantEvaluation.bonus(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return BonusForParticipantEvaluation.bonus(data);
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
