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

    private final Path protocolPath;
    private final ResultsByQuizMasterAndParticipant results;

    public ProtocolUpdater(
        final Path protocolPath,
        final ResultsByQuizMasterAndParticipant results,
        final List<Evaluation> quizMasterEvaluation,
        final List<Evaluation> participantsEvaluation
    ) {
        this.protocolPath = protocolPath;
        this.results = results;
    }

    public void updateProtocol(final Map<String, Integer> excused) throws IOException {
        final List<String> content = Files.readAllLines(this.protocolPath);
        final String student = ProtocolUpdater.parseStudent(content);
        if (student == null) {
            return;
        }
        final int currentExcused = excused.getOrDefault(student, 0);
        final String passedPercentage = this.calculatePassedPercentage(student, currentExcused);
        final String bonusQuizMaster1 = this.calculateBonusQuizMaster1(student, currentExcused);
        final String bonusQuizMaster2 = this.calculateBonusQuizMaster2(student, currentExcused);
        final String bonusQuizMaster3 = this.calculateBonusQuizMaster3(student, currentExcused);
        final String bonusParticipant1 = this.calculateBonusParticipant1(student, currentExcused);
        final String bonusParticipant2 = this.calculateBonusParticipant2(student, currentExcused);
        final String bonusParticipant3 = this.calculateBonusParticipant3(student, currentExcused);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.protocolPath.toFile()))) {
            for (final String line : content) {
                switch (line) {
                case "\\quizpassed{}":
                    writer.write(String.format("\\quizpassed{%s}", passedPercentage));
                    break;
                case "\\quizbonusi{}":
                    writer.write(String.format("\\quizbonusi{%s}", bonusQuizMaster1));
                    break;
                case "\\quizbonusii{}":
                    writer.write(String.format("\\quizbonusii{%s}", bonusQuizMaster2));
                    break;
                case "\\quizbonusiii{}":
                    writer.write(String.format("\\quizbonusiii{%s}", bonusQuizMaster3));
                    break;
                case "\\quizparticipantbonusi{}":
                    writer.write(String.format("\\quizparticipantbonusi{%s}", bonusParticipant1));
                    break;
                case "\\quizparticipantbonusii{}":
                    writer.write(String.format("\\quizparticipantbonusii{%s}", bonusParticipant2));
                    break;
                case "\\quizparticipantbonusiii{}":
                    writer.write(String.format("\\quizparticipantbonusiii{%s}", bonusParticipant3));
                    break;
                default:
                    writer.write(line);
                }
                writer.write("\n");
            }
        }
    }

    private String applyBonusCalculation(final ResultData data, final BonusCalculation calculation) {
        return calculation.calculation().apply(data) >= calculation.threshold() ? "1" : "0";
    }

    private String calculateBonusParticipant1(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForParticipantEvaluation.CALCULATIONS[0]
        );
    }

    private String calculateBonusParticipant2(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForParticipantEvaluation.CALCULATIONS[1]
        );
    }

    private String calculateBonusParticipant3(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForParticipantEvaluation.CALCULATIONS[2]
        );
    }

    private String calculateBonusQuizMaster1(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForQuizMasterEvaluation.CALCULATIONS[0]
        );
    }

    private String calculateBonusQuizMaster2(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForQuizMasterEvaluation.CALCULATIONS[1]
        );
    }

    private String calculateBonusQuizMaster3(final String student, final int excused) {
        return this.applyBonusCalculation(
            new ResultData(this.results, student, excused),
            ModernBonusForQuizMasterEvaluation.CALCULATIONS[2]
        );
    }

    private String calculatePassedPercentage(final String student, final int excused) {
        final double percentage =
            Passed6PercentageForQuizMasterEvaluation.passedPercentage(new ResultData(this.results, student, excused));
        if ((percentage % 1) == 0) {
            return String.valueOf((int)percentage);
        }
        return String.format(Locale.US, "%.1f", percentage);
    }

}
