package quizevaluator.evaluations;

import quizevaluator.*;

public class ModernBonusForParticipantEvaluation implements Evaluation {

    public static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                (results, name) -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(results, name),
                60
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed6PercentageForParticipantEvaluation.passedPercentage(results, name),
                100
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed9PercentageForParticipantEvaluation.passedPercentage(results, name),
                80
            )
        };

    private static int bonus(final ResultsByQuizMasterAndParticipant results, final String name) {
        return BonusCalculation.bonus(results, name, ModernBonusForParticipantEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultsByQuizMasterAndParticipant results, final String name) {
        return String.valueOf(ModernBonusForParticipantEvaluation.bonus(results, name));
    }

    @Override
    public Integer evaluation(final ResultsByQuizMasterAndParticipant results, final String name) {
        return ModernBonusForParticipantEvaluation.bonus(results, name);
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
