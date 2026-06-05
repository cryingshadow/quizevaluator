package quizevaluator.evaluations;

public class ModernBonusForParticipantEvaluation implements BonusEvaluation {

    @Override
    public BonusCalculation bonus1() {
        return new BonusCalculation(
            data -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(data),
            60
        );
    }

    @Override
    public BonusCalculation bonus2() {
        return new BonusCalculation(
            data -> (int)Passed6PercentageForParticipantEvaluation.passedPercentage(data),
            100
        );
    }

    @Override
    public BonusCalculation bonus3() {
        return new BonusCalculation(
            data -> (int)Passed9PercentageForParticipantEvaluation.passedPercentage(data),
            80
        );
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
