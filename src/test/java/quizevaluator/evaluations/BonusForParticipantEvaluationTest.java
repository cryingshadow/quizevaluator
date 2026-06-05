package quizevaluator.evaluations;

import java.util.*;

import org.testng.*;
import org.testng.annotations.Test;

import quizevaluator.*;

public class BonusForParticipantEvaluationTest {

    @Test
    public void evaluationTest() {
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS1, "Anna", 0, Collections.emptyList())
            ),
            2
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS1, "Bob", 0, Collections.emptyList())
            ),
            0
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS1, "Claire", 0, Collections.emptyList())
            ),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS1, "David", 0, Collections.emptyList())
            ),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS2, "Anna", 0, Collections.emptyList())
            ),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS2, "Bob", 0, Collections.emptyList())
            ),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS2, "Claire", 0, Collections.emptyList())
            ),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS2, "David", 0, Collections.emptyList())
            ),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS3, "Anna", 0, Collections.emptyList())
            ),
            3
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS3, "Bob", 0, Collections.emptyList())
            ),
            2
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS3, "Claire", 0, Collections.emptyList())
            ),
            1
        );
        Assert.assertEquals(
            new BonusForParticipantEvaluation().apply(
                new ResultData(Data.RESULTS3, "David", 0, Collections.emptyList())
            ),
            2
        );
    }

}
