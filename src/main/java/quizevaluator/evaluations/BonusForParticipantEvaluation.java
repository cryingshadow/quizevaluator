package quizevaluator.evaluations;

import quizevaluator.*;

public class BonusForParticipantEvaluation implements Evaluation {

    private static final BonusCalculation[] CALCULATIONS =
        new BonusCalculation[] {
            new BonusCalculation(
                (results, name) -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(results, name),
                50
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed5PercentageForParticipantEvaluation.passedPercentage(results, name),
                100
            ),
            new BonusCalculation(
                (results, name) -> (int)Passed8PercentageForParticipantEvaluation.passedPercentage(results, name),
                80
            )
        };

    private static int bonus(final ResultsByQuizMasterAndParticipant results, final String name) {
        return BonusCalculation.bonus(results, name, BonusForParticipantEvaluation.CALCULATIONS);
    }

    @Override
    public String cellText(final ResultsByQuizMasterAndParticipant results, final String name) {
        return String.valueOf(BonusForParticipantEvaluation.bonus(results, name));
    }

    @Override
    public Integer evaluation(final ResultsByQuizMasterAndParticipant results, final String name) {
        return BonusForParticipantEvaluation.bonus(results, name);
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
