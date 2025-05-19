package quizevaluator.evaluations;

import org.testng.*;
import org.testng.annotations.Test;

import quizevaluator.*;

public class BonusForParticipantEvaluationTest {

    @Test
    public void evaluationTest() {
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS1, "Anna", 0)),
            2
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS1, "Bob", 0)),
            0
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS1, "Claire", 0)),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS1, "David", 0)),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS2, "Anna", 0)),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS2, "Bob", 0)),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS2, "Claire", 0)),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS2, "David", 0)),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS3, "Anna", 0)),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS3, "Bob", 0)),
            2
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS3, "Claire", 0)),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().evaluation(new ResultData(Data.RESULTS3, "David", 0)),
            2
        );
    }

}
