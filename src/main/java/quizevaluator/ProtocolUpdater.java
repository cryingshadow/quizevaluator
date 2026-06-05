package quizevaluator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import quizevaluator.evaluations.*;

public class ProtocolUpdater {

    private static String parseStudent(final List<String> content) {
        for (final String line : content) {
            if (line.startsWith("\\newcommand{\\student}{")) {
                final String nameAndBrace = line.substring(22);
                return nameAndBrace.substring(0, nameAndBrace.length() - 1);
            }
        }
        return null;
    }

    private final Evaluations evaluations;

    private final Path protocolPath;

    private final ResultsByQuizMasterAndParticipant results;

    public ProtocolUpdater(
        final Path protocolPath,
        final ResultsByQuizMasterAndParticipant results,
        final Evaluations evaluations
    ) {
        this.protocolPath = protocolPath;
        this.results = results;
        this.evaluations = evaluations;
    }

    public void updateProtocol(final Map<String, Integer> excused, final List<String> canceled) throws IOException {
        final List<String> content = Files.readAllLines(this.protocolPath);
        final String student = ProtocolUpdater.parseStudent(content);
        if (student == null) {
            return;
        }
        final int currentExcused = excused.getOrDefault(student, 0);
        final ResultData resultData = new ResultData(this.results, student, currentExcused, canceled);
        final String passedPercentage = this.calculatePassedPercentage(resultData);
        final BonusEvaluation quizMasterBonus = this.evaluations.quizMasterEvaluations().bonus();
        final BonusEvaluation participantBonus = this.evaluations.participantEvaluations().bonus();
        final int bonusQuizMaster1 = quizMasterBonus.bonus1().apply(resultData);
        final int bonusQuizMaster2 = quizMasterBonus.bonus2().apply(resultData);
        final int bonusQuizMaster3 = quizMasterBonus.bonus3().apply(resultData);
        final int bonusParticipant1 = participantBonus.bonus1().apply(resultData);
        final int bonusParticipant2 = participantBonus.bonus2().apply(resultData);
        final int bonusParticipant3 = participantBonus.bonus3().apply(resultData);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.protocolPath.toFile()))) {
            for (final String line : content) {
                switch (line) {
                case "\\quizpassed{}":
                    writer.write(String.format("\\quizpassed{%s}", passedPercentage));
                    break;
                case "\\quizbonusi{}":
                    writer.write(String.format("\\quizbonusi{%d}", bonusQuizMaster1));
                    break;
                case "\\quizbonusii{}":
                    writer.write(String.format("\\quizbonusii{%d}", bonusQuizMaster2));
                    break;
                case "\\quizbonusiii{}":
                    writer.write(String.format("\\quizbonusiii{%d}", bonusQuizMaster3));
                    break;
                case "\\quizparticipantbonusi{}":
                    writer.write(String.format("\\quizparticipantbonusi{%d}", bonusParticipant1));
                    break;
                case "\\quizparticipantbonusii{}":
                    writer.write(String.format("\\quizparticipantbonusii{%d}", bonusParticipant2));
                    break;
                case "\\quizparticipantbonusiii{}":
                    writer.write(String.format("\\quizparticipantbonusiii{%d}", bonusParticipant3));
                    break;
                default:
                    writer.write(line);
                }
                writer.write("\n");
            }
        }
    }

    private String calculatePassedPercentage(final ResultData resultData) {
        final double percentage =
            resultData.passedPercentageQuizMaster(this.evaluations.quizMasterEvaluations().passedCount());
        if ((percentage % 1) == 0) {
            return String.valueOf((int)percentage);
        }
        return String.format(Locale.US, "%.1f", percentage);
    }

}
