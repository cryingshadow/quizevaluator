package quizevaluator;

import java.io.*;
import java.util.*;

public class ResultsByQuizMasterAndParticipant extends LinkedHashMap<String, Map<String, Integer>> {

    private static class QuizMasterRowOutput {

        private final List<String> names;
        private final Map<String, ResultData> resultData;
        private final BufferedWriter writer;

        private QuizMasterRowOutput(
            final List<String> names,
            final Map<String, ResultData> resultData,
            final BufferedWriter writer
        ) {
            this.writer = writer;
            this.names = names;
            this.resultData = resultData;
        }

        private void output(
            final String rowTitle,
            final CheckedConsumer<ResultData, IOException> output
        ) throws IOException {
            this.writer.write(rowTitle);
            for (final String name : this.names) {
                this.writer.write(';');
                output.accept(this.resultData.get(name));
            }
            this.writer.newLine();
        }
    }

    private static final long serialVersionUID = -3759177469532639760L;

    private static int computeScore(final int numOf50, final int participants) {
        if (numOf50 == 0) {
            return 0;
        }
        if (numOf50 == participants) {
            return 10;
        }
        return ((10 * numOf50) / participants) + 1;
    }

    public ResultsByQuizMasterAndParticipant(
        final SolutionsByQuizMaster solutionsByQuizMaster,
        final AnswersByQuizMasterAndParticipant answersByQuizMasterAndParticipant
    ) throws IOException {
        for (final String quizMaster : answersByQuizMasterAndParticipant.keySet()) {
            final Map<String, Integer> resultsByParticipant = new LinkedHashMap<String, Integer>();
            final Map<Integer, Integer> solution = solutionsByQuizMaster.get(quizMaster);
            if (solution == null) {
                throw new IOException("No solution found for quizmaster '" + quizMaster + "'");
            }
            final Map<String, Map<Integer, Integer>> answersByParticipant =
                answersByQuizMasterAndParticipant.get(quizMaster);
            for (final String participant : answersByParticipant.keySet()) {
                int result = 0;
                for (final Map.Entry<Integer, Integer> answer : answersByParticipant.get(participant).entrySet()) {
                    if (answer.getValue().equals(solution.get(answer.getKey()))) {
                        result++;
                    } else {
                        result--;
                    }
                }
                if (result < 0) {
                    result = 0;
                }
                resultsByParticipant.put(participant, result);
            }
            this.put(quizMaster, resultsByParticipant);
        }
    }

    public void output(final BufferedWriter writer) throws IOException {
        final List<String> names = new ArrayList<String>(this.keySet());
        final Map<String, ResultData> resultData = new LinkedHashMap<String, ResultData>();
        for (final String name : names) {
            resultData.put(name, new ResultData());
            writer.write(';');
            writer.write(name);
        }
        writer.write(
            ";Punkte gesamt;Punkte Prozent;Bestanden gesamt;Bestanden Prozent;Gut bestanden gesamt;Gut bestanden Prozent;Bonuspunkte"
        );
        writer.newLine();
        final int numOfQuiz = names.size() - 1;
        for (final String participant : names) {
            writer.write(participant);
            final ResultData dataParticipant = resultData.get(participant);
            for (final String quizMaster : names) {
                final Map<String, Integer> resultForQuizByParticipant = this.get(quizMaster);
                writer.write(';');
                if (resultForQuizByParticipant.containsKey(participant)) {
                    final ResultData dataQuizMaster = resultData.get(quizMaster);
                    dataQuizMaster.participantsOwnQuiz++;
                    final Integer points = resultForQuizByParticipant.get(participant);
                    dataQuizMaster.totalPointsOwnQuiz += points;
                    dataParticipant.totalPointsOtherQuiz += points;
                    if (points >= 5) {
                        dataQuizMaster.numOf50OwnQuiz++;
                        dataParticipant.numOf50OtherQuiz++;
                        if (points >= 8) {
                            dataQuizMaster.numOf80OwnQuiz++;
                            dataParticipant.numOf80OtherQuiz++;
                        }
                    }
                    writer.write(points.toString());
                }
            }
            writer.write(';');
            writer.write(String.valueOf(dataParticipant.totalPointsOtherQuiz));
            final double pointsPercent = 100 * dataParticipant.totalPointsOtherQuiz / (double)(10 * numOfQuiz);
            if (pointsPercent >= 50) {
                dataParticipant.bonusOther++;
            }
            writer.write(';');
            writer.write(String.format("%.2f", pointsPercent));
            writer.write(';');
            writer.write(String.valueOf(dataParticipant.numOf50OtherQuiz));
            final double passedPercent = 100 * dataParticipant.numOf50OtherQuiz / (double)numOfQuiz;
            if (numOfQuiz > 0 && dataParticipant.numOf50OtherQuiz == numOfQuiz) {
                dataParticipant.bonusOther++;
            }
            writer.write(';');
            writer.write(String.format("%.2f", passedPercent));
            writer.write(';');
            writer.write(String.valueOf(dataParticipant.numOf80OtherQuiz));
            final double wellPercent = 100 * dataParticipant.numOf80OtherQuiz / (double)numOfQuiz;
            if (wellPercent >= 80) {
                dataParticipant.bonusOther++;
            }
            writer.write(';');
            writer.write(String.format("%.2f", wellPercent));
            writer.write(';');
            writer.write(String.valueOf(dataParticipant.bonusOther));
            writer.newLine();
        }
        final QuizMasterRowOutput row = new QuizMasterRowOutput(names, resultData, writer);
        row.output("Punkte gesamt", dataQuizMaster -> writer.write(String.valueOf(dataQuizMaster.totalPointsOwnQuiz)));
        row.output(
            "Punkte Prozent",
            dataQuizMaster -> {
                final double pointsPercent =
                    100 * dataQuizMaster.totalPointsOwnQuiz / (double)(10 * dataQuizMaster.participantsOwnQuiz);
                if (pointsPercent >= 75) {
                    dataQuizMaster.bonusOwn++;
                }
                writer.write(String.format("%.2f", pointsPercent));
            }
        );
        row.output("Bestanden gesamt", dataQuizMaster -> writer.write(String.valueOf(dataQuizMaster.numOf50OwnQuiz)));
        row.output(
            "Bestanden Prozent",
            dataQuizMaster -> {
                final double passedPercent =
                    100 * dataQuizMaster.numOf50OwnQuiz / (double)dataQuizMaster.participantsOwnQuiz;
                if (
                    dataQuizMaster.participantsOwnQuiz > 0
                    && dataQuizMaster.numOf50OwnQuiz == dataQuizMaster.participantsOwnQuiz
                ) {
                    dataQuizMaster.bonusOwn++;
                }
                writer.write(String.format("%.2f", passedPercent));
            }
        );
        row.output(
            "Bestanden Bewertung",
            dataQuizMaster ->
                writer.write(
                    String.valueOf(
                        ResultsByQuizMasterAndParticipant.computeScore(
                            dataQuizMaster.numOf50OwnQuiz,
                            dataQuizMaster.participantsOwnQuiz
                        )
                    )
                )
        );
        row.output(
            "Gut bestanden gesamt",
            dataQuizMaster -> writer.write(String.valueOf(dataQuizMaster.numOf80OwnQuiz))
        );
        row.output(
            "Gut bestanden Prozent",
            dataQuizMaster -> {
                final double wellPercent =
                    100 * dataQuizMaster.numOf80OwnQuiz / (double)dataQuizMaster.participantsOwnQuiz;
                if (wellPercent >= 80) {
                    dataQuizMaster.bonusOwn++;
                }
                writer.write(String.format("%.2f", wellPercent));
            }
        );
        row.output("Bonus", dataQuizMaster -> writer.write(String.valueOf(dataQuizMaster.bonusOwn)));
        writer.newLine();
        row.output(
            "Bewertung",
            dataQuizMaster ->
                writer.write(
                    String.valueOf(
                        dataQuizMaster.bonusOwn
                        + dataQuizMaster.bonusOther
                        + ResultsByQuizMasterAndParticipant.computeScore(
                            dataQuizMaster.numOf50OwnQuiz,
                            dataQuizMaster.participantsOwnQuiz
                        )
                    )
                )
        );
    }

}
