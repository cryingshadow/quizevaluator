package quizevaluator;

import java.io.*;
import java.util.*;

import quizevaluator.evaluations.*;

public class CSVWriter {

    private final BufferedWriter writer;

    public CSVWriter(final BufferedWriter writer) {
        this.writer = writer;
    }

    public void writeCSV(
        final ResultsByQuizMasterAndParticipant resultsByQuizMasterAndParticipant,
        final List<Evaluation> quizMasterEvaluations,
        final List<Evaluation> participantEvaluations,
        final Map<String, Integer> excused
    ) throws IOException {
        final List<String> names = new ArrayList<String>(resultsByQuizMasterAndParticipant.keySet());
        final Map<String, Map<String, String>> cellsByQuizMasterAndTitle =
            new LinkedHashMap<String, Map<String, String>>();
        final Map<String, List<String>> participantCells = new LinkedHashMap<String, List<String>>();
        final List<String> quizMasterTitles = new ArrayList<String>();
        final List<String> participantTitles = new ArrayList<String>();
        final Map<String, Integer> totalPointsByName = new LinkedHashMap<String, Integer>();
        for (final Evaluation quizMasterEvaluation : quizMasterEvaluations) {
            quizMasterTitles.add(quizMasterEvaluation.title());
        }
        for (final Evaluation participantEvaluation : participantEvaluations) {
            participantTitles.add(participantEvaluation.title());
        }
        for (final String name : names) {
            this.writer.write(';');
            this.writer.write(name);
            cellsByQuizMasterAndTitle.put(name, new LinkedHashMap<String, String>());
            participantCells.put(name, new ArrayList<String>());
            totalPointsByName.put(name, 0);
            final int currentExcused = excused.getOrDefault(name, 0);
            for (final Evaluation quizMasterEvaluation : quizMasterEvaluations) {
                cellsByQuizMasterAndTitle.get(name).put(
                    quizMasterEvaluation.title(),
                    quizMasterEvaluation.cellText(
                        new ResultData(resultsByQuizMasterAndParticipant, name, currentExcused)
                    )
                );
                totalPointsByName.put(
                    name,
                    totalPointsByName.get(name)
                    + quizMasterEvaluation.evaluation(
                        new ResultData(resultsByQuizMasterAndParticipant, name, currentExcused)
                    )
                );
            }
            for (final Evaluation participantEvaluation : participantEvaluations) {
                participantCells.get(name).add(
                    participantEvaluation.cellText(
                        new ResultData(resultsByQuizMasterAndParticipant, name, currentExcused)
                    )
                );
                totalPointsByName.put(
                    name,
                    totalPointsByName.get(name)
                    + participantEvaluation.evaluation(
                        new ResultData(resultsByQuizMasterAndParticipant, name, currentExcused)
                    )
                );
            }
        }
        for (final String title : participantTitles) {
            this.writer.write(";");
            this.writer.write(title);
        }
        this.writer.newLine();
        for (final String participant : names) {
            this.writer.write(participant);
            for (final String quizMaster : names) {
                final Map<String, Integer> resultForQuizByParticipant =
                    resultsByQuizMasterAndParticipant.get(quizMaster);
                this.writer.write(';');
                if (resultForQuizByParticipant.containsKey(participant)) {
                    this.writer.write(resultForQuizByParticipant.get(participant).toString());
                }
            }
            for (final String cellText : participantCells.get(participant)) {
                this.writer.write(';');
                this.writer.write(cellText);
            }
            this.writer.newLine();
        }
        for (final String title : quizMasterTitles) {
            this.writer.write(title);
            for (final String quizMaster : names) {
                this.writer.write(";");
                this.writer.write(cellsByQuizMasterAndTitle.get(quizMaster).get(title));
            }
            this.writer.newLine();
        }
        this.writer.newLine();
        this.writer.write("Bewertung");
        for (final String name : names) {
            this.writer.write(";");
            this.writer.write(String.valueOf(totalPointsByName.get(name)));
        }
        this.writer.newLine();
    }

}
