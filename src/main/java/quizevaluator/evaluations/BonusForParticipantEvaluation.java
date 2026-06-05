package quizevaluator.evaluations;

public class BonusForParticipantEvaluation implements BonusEvaluation {

    @Override
    public BonusCalculation bonus1() {
        return new BonusCalculation(
            data -> (int)PointsPercentageForParticipantEvaluation.pointsPercentage(data),
            50
        );
    }

    @Override
    public BonusCalculation bonus2() {
        return new BonusCalculation(
            data -> (int)Passed5PercentageForParticipantEvaluation.passedPercentage(data),
            100
        );
    }

    @Override
    public BonusCalculation bonus3() {
        return new BonusCalculation(
            data -> (int)Passed8PercentageForParticipantEvaluation.passedPercentage(data),
            80
        );
    }

    @Override
    public String title() {
        return "Bonuspunkte";
    }

}
