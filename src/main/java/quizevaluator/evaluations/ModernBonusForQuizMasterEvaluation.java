package quizevaluator.evaluations;

public class ModernBonusForQuizMasterEvaluation implements Evaluation {

    public static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                data -> (int)PointsPercentageForQuizMasterEvaluation.pointsPercentage(data),
                80
            ),
            new BonusCalculation(
                data -> (int)Passed6PercentageForQuizMasterEvaluation.passedPercentage(data),
                100
            ),
            new BonusCalculation(
                data -> (int)Passed9PercentageForQuizMasterEvaluation.passedPercentage(data),
                80
            )
        };

    private static int bonus(final ResultData data) {
        return BonusCalculation.bonus(data, ModernBonusForQuizMasterEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(ModernBonusForQuizMasterEvaluation.bonus(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return ModernBonusForQuizMasterEvaluation.bonus(data);
    }

    @Override
    public String title() {
        return "Bonus";
    }

}
