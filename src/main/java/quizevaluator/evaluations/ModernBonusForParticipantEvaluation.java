package quizevaluator.evaluations;

public class ModernBonusForParticipantEvaluation implements Evaluation {

    public static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                data -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(data),
                60
            ),
            new BonusCalculation(
                data -> (int)Passed6PercentageForParticipantEvaluation.passedPercentage(data),
                100
            ),
            new BonusCalculation(
                data -> (int)Passed9PercentageForParticipantEvaluation.passedPercentage(data),
                80
            )
        };

    private static int bonus(final ResultData data) {
        return BonusCalculation.bonus(data, ModernBonusForParticipantEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(ModernBonusForParticipantEvaluation.bonus(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return ModernBonusForParticipantEvaluation.bonus(data);
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
