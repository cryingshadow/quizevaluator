package quizevaluator.evaluations;

import java.util.*;
import java.util.function.*;

public interface Evaluation {

    public static int passedCountParticipant(
        final ResultData data,
        final int passLimit
    ) {
        return data
            .results()
            .values()
            .stream()
            .mapToInt(map -> map.getOrDefault(data.name(), 0) >= passLimit ? 1 : 0)
            .sum();
    }

    public static int passedCountQuizMaster(
        final ResultData data,
        final int passLimit
    ) {
        if (!data.results().containsKey(data.name())) {
            System.out.print("No data found for ");
            System.out.print(data.name());
            System.out.println("!");
        }
        return data
            .results()
            .getOrDefault(data.name(), Collections.emptyMap())
            .values()
            .stream()
            .mapToInt(points -> points >= passLimit ? 1 : 0)
            .sum();
    }

    public static double passedPercentageParticipant(
        final ResultData data,
        final Function<ResultData, Integer> countFunction
    ) {
        final double passedTimes100 = countFunction.apply(data) * 100;
        final int total = (data.results().size() - 1 - data.excused());
        return passedTimes100 / total;
    }

    public static double passedPercentageQuizMaster(
        final ResultData data,
        final Function<ResultData, Integer> countFunction
    ) {
        final double passedTimes100 = countFunction.apply(data) * 100;
        if (!data.results().containsKey(data.name())) {
            System.out.print("No data found for ");
            System.out.print(data.name());
            System.out.println("!");
        }
        final int total = data.results().getOrDefault(data.name(), Collections.emptyMap()).size();
        return passedTimes100 / total;
    }

    String cellText(ResultData data);

    Integer evaluation(ResultData data);

    String title();

}
