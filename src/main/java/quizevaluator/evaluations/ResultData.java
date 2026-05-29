package quizevaluator.evaluations;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import quizevaluator.*;

public record ResultData(ResultsByQuizMasterAndParticipant results, String name, int excused, List<String> canceled) {

    public int passedCountParticipant(final int passLimit) {
        return this
            .getUncanceledOtherQuizData()
            .mapToInt(entry -> entry.getValue().getOrDefault(this.name(), 0) >= passLimit ? 1 : 0)
            .sum();
    }

    public int passedCountQuizMaster(final int passLimit) {
        if (!this.results().containsKey(this.name())) {
            System.out.print("No data found for ");
            System.out.print(this.name());
            System.out.println("!");
        }
        return this
            .results()
            .getOrDefault(this.name(), Collections.emptyMap())
            .values()
            .stream()
            .mapToInt(points -> points >= passLimit ? 1 : 0)
            .sum();
    }

    public int totalPointsInOwnQuiz() {
        return this.results().get(this.name()).values().stream().mapToInt(Integer::intValue).sum();
    }

    public int totalPointsAsParticipant() {
        return this
            .getUncanceledOtherQuizData()
            .mapToInt(entry -> entry.getValue().getOrDefault(this.name(), 0).intValue())
            .sum();
    }

    public double passedPercentageParticipant(final Function<ResultData, Integer> countFunction) {
        final double passedTimes100 = countFunction.apply(this) * 100;
        return passedTimes100 / this.totalNumberOfQuizzesAsParticipant();
    }

    public double passedPercentageQuizMaster(final Function<ResultData, Integer> countFunction) {
        final double passedTimes100 = countFunction.apply(this) * 100;
        if (!this.results().containsKey(this.name())) {
            System.out.print("No data found for ");
            System.out.print(this.name());
            System.out.println("!");
        }
        return passedTimes100 / this.totalNumberOfParticipantsInOwnQuiz();
    }

    private Stream<Map.Entry<String, Map<String, Integer>>> getUncanceledOtherQuizData() {
        return this
            .results()
            .entrySet()
            .stream()
            .filter(entry -> !this.canceled().contains(entry.getKey()) && !this.name().equals(entry.getKey()));
    }

    public int totalNumberOfQuizzesAsParticipant() {
        final List<String> keys = new LinkedList<String>(this.results().keySet());
        keys.removeAll(this.canceled());
        keys.remove(this.name());
        return ((int)this.getUncanceledOtherQuizData().count()) - this.excused();
    }

    public int totalNumberOfParticipantsInOwnQuiz() {
        return this.results().getOrDefault(this.name(), Collections.emptyMap()).size();
    }

}
