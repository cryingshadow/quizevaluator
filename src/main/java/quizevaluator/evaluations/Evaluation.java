package quizevaluator.evaluations;

import java.util.function.*;

import quizevaluator.*;

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
        return data.results().get(data.name()).values().stream().mapToInt(points -> points >= passLimit ? 1 : 0).sum();
    }

    public static double passedPercentageParticipant(
        final ResultData data,
        final BiFunction<ResultsByQuizMasterAndParticipant, String, Integer> countFunction
    ) {
        final double passedTimes100 = countFunction.apply(data.results(), data.name()) * 100;
        final int total = (data.results().size() - 1 - data.excused());
        return passedTimes100 / total;
    }

    public static double passedPercentageQuizMaster(
        final ResultData data,
        final BiFunction<ResultsByQuizMasterAndParticipant, String, Integer> countFunction
    ) {
        final double passedTimes100 = countFunction.apply(data.results(), data.name()) * 100;
        final int total = data.results().get(data.name()).size();
        return passedTimes100 / total;
    }

    String cellText(ResultData data);

    Integer evaluation(ResultData data);

    String title();

}
