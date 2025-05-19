package quizevaluator.evaluations;

public class BonusForQuizMasterEvaluation implements Evaluation {

    private static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                data -> (int)PointsPercentageForQuizMasterEvaluation.pointsPercentage(data),
                75
            ),
            new BonusCalculation(
                data -> (int)Passed5PercentageForQuizMasterEvaluation.passedPercentage(data),
                100
            ),
            new BonusCalculation(
                data -> (int)Passed8PercentageForQuizMasterEvaluation.passedPercentage(data),
                80
            )
        };

    private static int bonus(final ResultData data) {
        return BonusCalculation.bonus(data, BonusForQuizMasterEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultData data) {
        return String.valueOf(BonusForQuizMasterEvaluation.bonus(data));
    }

    @Override
    public Integer evaluation(final ResultData data) {
        return BonusForQuizMasterEvaluation.bonus(data);
    }

    @Override
    public String title() {
        return "Bonus";
    }

}
