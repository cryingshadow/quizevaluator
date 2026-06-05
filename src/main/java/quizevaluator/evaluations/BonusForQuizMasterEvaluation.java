package quizevaluator.evaluations;

public class BonusForQuizMasterEvaluation implements BonusEvaluation {

    @Override
    public BonusCalculation bonus1() {
        return new BonusCalculation(
            data -> (int)PointsPercentageForQuizMasterEvaluation.pointsPercentage(data),
            75
        );
    }

    @Override
    public BonusCalculation bonus2() {
        return new BonusCalculation(
            data -> (int)Passed5PercentageForQuizMasterEvaluation.passedPercentage(data),
            100
        );
    }

    @Override
    public BonusCalculation bonus3() {
        return new BonusCalculation(
            data -> (int)Passed8PercentageForQuizMasterEvaluation.passedPercentage(data),
            80
        );
    }

    @Override
    public String title() {
        return "Bonus";
    }

}
