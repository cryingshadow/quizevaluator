package quizevaluator;

import java.util.*;

public class ResultsByQuizMasterAndParticipant extends LinkedHashMap<String, Map<String, Integer>> {

    private static final long serialVersionUID = -3759177469532639760L;

    public ResultsByQuizMasterAndParticipant(
        final SolutionsByQuizMaster solutionsByQuizMaster,
        final AnswerDataByQuizMasterAndParticipant answersByQuizMasterAndParticipant,
        final ResultComputation resultComputation
    ) {
        for (final String quizMaster : solutionsByQuizMaster.keySet()) {
            final Map<String, Integer> resultsByParticipant = new LinkedHashMap<String, Integer>();
            final Map<String, AnswerData> answersByParticipant =
                answersByQuizMasterAndParticipant.getOrDefault(quizMaster, Collections.emptyMap());
            for (final String participant : answersByParticipant.keySet()) {
                resultsByParticipant.put(participant, resultComputation.apply(answersByParticipant.get(participant)));
            }
            this.put(quizMaster, resultsByParticipant);
        }
    }

    ResultsByQuizMasterAndParticipant(final String[] names, final int[][] results) {
        for (int quizMaster = 0; quizMaster < names.length; quizMaster++) {
            final Map<String, Integer> resultsByParticipant = new LinkedHashMap<String, Integer>();
            for (int participant = 0; participant < names.length; participant++) {
                final int result = results[participant][quizMaster];
                if (result >= 0) {
                    resultsByParticipant.put(names[participant], result);
                }
            }
            this.put(names[quizMaster], resultsByParticipant);
        }
    }

}
