package quizevaluator.evaluations;

import quizevaluator.*;

public class ModernBonusForQuizMasterEvaluation implements Evaluation {

    public static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                (results, name) -> (int)PointsPercentageForQuizMasterEvaluation.pointsPercentage(results, name),
                80
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed6PercentageForQuizMasterEvaluation.passedPercentage(results, name),
                100
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed9PercentageForQuizMasterEvaluation.passedPercentage(results, name),
                80
            )
        };

    private static int bonus(final ResultsByQuizMasterAndParticipant results, final String name) {
        return BonusCalculation.bonus(results, name, ModernBonusForQuizMasterEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultsByQuizMasterAndParticipant results, final String name) {
        return String.valueOf(ModernBonusForQuizMasterEvaluation.bonus(results, name));
    }

    @Override
    public Integer evaluation(final ResultsByQuizMasterAndParticipant results, final String name) {
        return ModernBonusForQuizMasterEvaluation.bonus(results, name);
    }

    @Override
    public String title() {
        return "Bonus";
    }

}
