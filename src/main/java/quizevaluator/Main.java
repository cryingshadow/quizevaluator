package quizevaluator;

import java.io.*;
import java.util.*;

import quizevaluator.evaluations.*;

public class Main {

    static final List<Evaluation> PARTICIPANTS_EVALUATIONS_NEW =
        List.of(
            new TotalPointsForParticipantEvaluation(),
            new PointsPercentageForParticipantEvaluation(),
            new Passed6CountForParticipantEvaluation(),
            new Passed6PercentageForParticipantEvaluation(),
            new Passed9CountForParticipantEvaluation(),
            new Passed9PercentageForParticipantEvaluation(),
            new ModernBonusForParticipantEvaluation()
        );

    static final List<Evaluation> PARTICIPANTS_EVALUATIONS_OLD =
        List.of(
            new TotalPointsForParticipantEvaluation(),
            new PointsPercentageForParticipantEvaluation(),
            new Passed5CountForParticipantEvaluation(),
            new Passed5PercentageForParticipantEvaluation(),
            new Passed8CountForParticipantEvaluation(),
            new Passed8PercentageForParticipantEvaluation(),
            new BonusForParticipantEvaluation()
        );

    static final List<Evaluation> QUIZ_MASTER_EVALUATIONS_NEW =
        List.of(
            new TotalPointsForQuizMasterEvaluation(),
            new PointsPercentageForQuizMasterEvaluation(),
            new Passed6CountForQuizMasterEvaluation(),
            new Passed6PercentageForQuizMasterEvaluation(),
            new Passed6TotalForQuizMasterEvaluation(),
            new Passed9CountForQuizMasterEvaluation(),
            new Passed9PercentageForQuizMasterEvaluation(),
            new ModernBonusForQuizMasterEvaluation()
        );

    static final List<Evaluation> QUIZ_MASTER_EVALUATIONS_OLD =
        List.of(
            new TotalPointsForQuizMasterEvaluation(),
            new PointsPercentageForQuizMasterEvaluation(),
            new Passed5CountForQuizMasterEvaluation(),
            new Passed5PercentageForQuizMasterEvaluation(),
            new Passed5TotalForQuizMasterEvaluation(),
            new Passed8CountForQuizMasterEvaluation(),
            new Passed8PercentageForQuizMasterEvaluation(),
            new BonusForQuizMasterEvaluation()
        );

    public static void main(final String[] args) throws IOException {
        if (args == null || args.length < 3 || args.length > 4) {
            throw new IllegalArgumentException(
                "You must provide a file with correct answers, a diectory with files containing given answers, and a "
                + "file for the output!\nYou may additionally specify an execution mode (old/new/full)."
            );
        }
        final FormsExcelToCsvConverter excelConverter = new FormsExcelToCsvConverter();
        final SolutionsByQuizMaster solutionsByQuizMaster = Main.parseSolutions(args[0]);
        final AnswerDataByQuizMasterAndParticipant answerDataByQuizMasterAndParticipant =
            new AnswerDataByQuizMasterAndParticipant();
        for (File file : new File(args[1]).listFiles()) {
            if (excelConverter.isExcelFile(file)) {
                file = excelConverter.convert(file);
            }
            try (BufferedReader answersReader = new BufferedReader(new FileReader(file))) {
                answerDataByQuizMasterAndParticipant.parseAnswers(answersReader, solutionsByQuizMaster);
            } catch (RuntimeException | IOException e) {
                throw new IOException("Error in file " + file.getAbsolutePath(), e);
            }
        }
        final ExecutionMode mode = args.length == 4 ? ExecutionMode.from(args[3]) : ExecutionMode.FULL;
        final ResultsByQuizMasterAndParticipant results =
            new ResultsByQuizMasterAndParticipant(
                answerDataByQuizMasterAndParticipant,
                Main.selectResultComputation(mode)
            );
        final List<Evaluation> quizMasterEvaluation = Main.selectQuizMasterEvaluation(mode);
        final List<Evaluation> participantsEvaluation = Main.selectParticipantsEvaluation(mode);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]))) {
            new CSVWriter(writer).writeCSV(results, quizMasterEvaluation, participantsEvaluation);
        }
        if (mode == ExecutionMode.FULL) {
            for (
                final File protocol :
                    new File(args[0]).getAbsoluteFile().toPath().getParent().resolve("protocols").toFile().listFiles()
            ) {
                if (protocol.getName().endsWith(".tex")) {
                    new ProtocolUpdater(
                        protocol.toPath(),
                        results,
                        quizMasterEvaluation,
                        participantsEvaluation
                    ).updateProtocol();
                }
            }
        }
    }

    private static SolutionsByQuizMaster parseSolutions(final String file) throws IOException {
        try (BufferedReader solutionReader = new BufferedReader(new FileReader(file))) {
            return new SolutionsByQuizMaster(solutionReader);
        }
    }

    private static List<Evaluation> selectParticipantsEvaluation(final ExecutionMode mode) {
        switch (mode) {
        case OLD:
            return Main.PARTICIPANTS_EVALUATIONS_OLD;
        case NEW:
        case FULL:
            return Main.PARTICIPANTS_EVALUATIONS_NEW;
        default:
            throw new IllegalStateException("New execution mode without complete implementation detected!");
        }
    }

    private static List<Evaluation> selectQuizMasterEvaluation(final ExecutionMode mode) {
        switch (mode) {
        case OLD:
            return Main.QUIZ_MASTER_EVALUATIONS_OLD;
        case NEW:
        case FULL:
            return Main.QUIZ_MASTER_EVALUATIONS_NEW;
        default:
            throw new IllegalStateException("New execution mode without complete implementation detected!");
        }
    }

    private static ResultComputation selectResultComputation(final ExecutionMode mode) {
        switch (mode) {
        case OLD:
            return new OldSchoolMCResultComputation();
        case NEW:
        case FULL:
            return new ModernMCResultComputation();
        default:
            throw new IllegalStateException("New execution mode without complete implementation detected!");
        }
    }

}
