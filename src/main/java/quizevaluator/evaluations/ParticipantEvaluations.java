package quizevaluator.evaluations;

import java.util.*;
import java.util.function.*;

public record ParticipantEvaluations(
    IntegerEvaluation pointsTotal,
    PercentageEvaluation pointsPercentage,
    IntegerEvaluation passedCount,
    PercentageEvaluation passedPercentage,
    IntegerEvaluation wellPassedTotal,
    PercentageEvaluation wellPassedPercentage,
    BonusEvaluation bonus
) implements Collection<Evaluation>, Function<ResultData, Integer> {

    @Override
    public Iterator<Evaluation> iterator() {
        return this.toList().iterator();
    }

    public List<Evaluation> toList() {
        return List.of(
            this.pointsTotal(),
            this.pointsPercentage(),
            this.passedCount(),
            this.passedPercentage(),
            this.wellPassedTotal(),
            this.wellPassedPercentage(),
            this.bonus()
        );
    }

    @Override
    public int size() {
        return this.toList().size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(final Object o) {
        return this.toList().contains(o);
    }

    @Override
    public Object[] toArray() {
        return this.toList().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return this.toList().toArray(a);
    }

    @Override
    public boolean add(final Evaluation e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.toList().containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends Evaluation> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer apply(final ResultData data) {
        return this.bonus().apply(data);
    }

}
