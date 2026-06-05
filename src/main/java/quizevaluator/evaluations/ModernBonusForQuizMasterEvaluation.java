package quizevaluator.evaluations;

public class ModernBonusForQuizMasterEvaluation implements BonusEvaluation {

    @Override
    public BonusCalculation bonus1() {
        return new BonusCalculation(
            data -> (int)PointsPercentageForQuizMasterEvaluation.pointsPercentage(data),
            80
        );
    }

    @Override
    public BonusCalculation bonus2() {
        return new BonusCalculation(
            data -> (int)Passed6PercentageForQuizMasterEvaluation.passedPercentage(data),
            100
        );
    }

    @Override
    public BonusCalculation bonus3() {
        return new BonusCalculation(
            data -> (int)Passed9PercentageForQuizMasterEvaluation.passedPercentage(data),
            80
        );
    }

    @Override
    public String title() {
        return "Bonus";
    }

}
