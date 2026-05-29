package quizevaluator.evaluations;

public interface Evaluation {

    String cellText(ResultData data);

    Integer evaluation(ResultData data);

    String title();

}
