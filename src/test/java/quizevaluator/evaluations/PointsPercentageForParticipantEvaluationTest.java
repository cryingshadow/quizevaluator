package quizevaluator.evaluations;

import java.util.*;

import org.testng.*;
import org.testng.annotations.*;

import quizevaluator.*;

public class PointsPercentageForParticipantEvaluationTest {

    @DataProvider
    public Object[][] pointsPercentageData() {
        return new Object[][] {
            {new ResultData(Data.RESULTS3, "Claire", 0, List.of()), 63.333},
            {new ResultData(Data.RESULTS3, "Claire", 1, List.of()), 95.0},
            {new ResultData(Data.RESULTS3, "Claire", 0, List.of("David")), 95.0}
        };
    }

    @Test( dataProvider = "pointsPercentageData" )
    public void pointsPercentageTest(final ResultData data, final double expected) {
        Assert.assertEquals(PointsPercentageForParticipantEvaluation.pointsPercentage(data), expected, 0.01);
    }

}
