package quizevaluator.evaluations;

import java.util.*;
import java.util.function.*;

import org.testng.*;
import org.testng.annotations.*;

import quizevaluator.*;

public class ResultDataTest {

    @DataProvider
    public Object[][] passedCountParticipantData() {
        return new Object[][] {
            {new ResultData(Data.RESULTS3, "Anna", 0, List.of()), 6, 3},
            {new ResultData(Data.RESULTS3, "Anna", 0, List.of("David")), 6, 2},
            {new ResultData(Data.RESULTS3, "Claire", 0, List.of()), 6, 2},
            {new ResultData(Data.RESULTS3, "Claire", 1, List.of()), 6, 2},
            {new ResultData(Data.RESULTS3, "Claire", 0, List.of("David")), 6, 2}
        };
    }

    @Test(dataProvider = "passedCountParticipantData")
    public void passedCountParticipantTest(final ResultData data, final int passLimit, final int expected) {
        Assert.assertEquals(data.passedCountParticipant(passLimit), expected);
    }

    @DataProvider
    public Object[][] passedCountQuizMasterData() {
        return new Object[][] {
            {new ResultData(Data.RESULTS3, "Claire", 0, List.of()), 6, 3},
            {new ResultData(Data.RESULTS3, "Claire", 1, List.of()), 6, 3},
            {new ResultData(Data.RESULTS3, "Claire", 1, List.of("Claire")), 6, 3},
            {new ResultData(Data.RESULTS3, "David", 0, List.of()), 6, 1},
            {new ResultData(Data.RESULTS3, "David", 1, List.of()), 6, 1},
            {new ResultData(Data.RESULTS3, "David", 0, List.of("David")), 6, 1}
        };
    }

    @Test(dataProvider = "passedCountQuizMasterData")
    public void passedCountQuizMasterTest(final ResultData data, final int passLimit, final int expected) {
        Assert.assertEquals(data.passedCountQuizMaster(passLimit), expected);
    }

    @DataProvider
    public Object[][] passedPercentageParticipantData() {
        return new Object[][] {
            {
                new ResultData(Data.RESULTS3, "Claire", 0, List.of()),
                (Function<ResultData, Integer>)Passed6CountForParticipantEvaluation::passedCount,
                66.666
            },
            {
                new ResultData(Data.RESULTS3, "Claire", 1, List.of()),
                (Function<ResultData, Integer>)Passed6CountForParticipantEvaluation::passedCount,
                100.0
            },
            {
                new ResultData(Data.RESULTS3, "Claire", 0, List.of("David")),
                (Function<ResultData, Integer>)Passed6CountForParticipantEvaluation::passedCount,
                100.0
            }
        };
    }

    @Test(dataProvider = "passedPercentageParticipantData")
    public void passedPercentageParticipantTest(
        final ResultData data,
        final Function<ResultData, Integer> countFunction,
        final double expected
    ) {
        Assert.assertEquals(data.passedPercentageParticipant(countFunction), expected, 0.01);
    }

    @DataProvider
    public Object[][] passedPercentageQuizMasterData() {
        return new Object[][] {
            {
                new ResultData(Data.RESULTS2, "Bob", 0, List.of()),
                (Function<ResultData, Integer>)Passed6CountForQuizMasterEvaluation::passedCount,
                66.666
            },
            {
                new ResultData(Data.RESULTS2, "David", 1, List.of()),
                (Function<ResultData, Integer>)Passed6CountForQuizMasterEvaluation::passedCount,
                0.0
            },
            {
                new ResultData(Data.RESULTS3, "David", 0, List.of("David")),
                (Function<ResultData, Integer>)Passed6CountForQuizMasterEvaluation::passedCount,
                50.0
            }
        };
    }

    @Test(dataProvider = "passedPercentageQuizMasterData")
    public void passedPercentageQuizMasterTest(
        final ResultData data,
        final Function<ResultData, Integer> countFunction,
        final double expected
    ) {
        Assert.assertEquals(data.passedPercentageQuizMaster(countFunction), expected, 0.01);
    }

}
