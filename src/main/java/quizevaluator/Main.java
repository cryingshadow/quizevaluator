package quizevaluator;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import clit.*;
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

    public static void main(final String[] args)
    throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final CLITamer<Flag> tamer = new CLITamer<Flag>(Flag.class, Set.of(Flag.SOLUTIONS, Flag.ANSWERS, Flag.OUTPUT));
        if (args == null || args.length < 1) {
            System.out.println(tamer.getParameterDescriptions());
            return;
        }
        final Parameters<Flag> options = tamer.parse(args);
        final SolutionsByQuizMaster solutionsByQuizMaster = Main.parseSolutions(options.get(Flag.SOLUTIONS));
        final ExecutionMode mode = ExecutionMode.from(options.getOrDefault(Flag.MODE, "FULL"));
        final ResultsByQuizMasterAndParticipant results = Main.computeResults(solutionsByQuizMaster, mode, options);
        final Map<String, Integer> excused =
            Main.parseExcuses(Optional.ofNullable(options.get(Flag.EXCUSES)).map(File::new));
        final List<Evaluation> quizMasterEvaluation = Main.selectQuizMasterEvaluation(mode);
        final List<Evaluation> participantsEvaluation = Main.selectParticipantsEvaluation(mode);
        final List<String> canceled = Main.parseCanceled(Optional.ofNullable(options.get(Flag.CANCELED)).map(File::new));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(options.get(Flag.OUTPUT)))) {
            new CSVWriter(writer).writeCSV(results, quizMasterEvaluation, participantsEvaluation, excused, canceled);
        }
        if (mode == ExecutionMode.FULL) {
            Main.updateProtocols(options, results, quizMasterEvaluation, participantsEvaluation, excused, canceled);
        }
    }

    private static ResultsByQuizMasterAndParticipant computeResults(
        final SolutionsByQuizMaster solutionsByQuizMaster,
        final ExecutionMode mode,
        final Parameters<Flag> options
    ) throws IOException {
        final AnswerDataByQuizMasterAndParticipant answerDataByQuizMasterAndParticipant =
            new AnswerDataByQuizMasterAndParticipant();
        for (File file : new File(options.get(Flag.ANSWERS)).listFiles()) {
            if (FormsExcelToCsvConverter.isExcelFile(file)) {
                file = FormsExcelToCsvConverter.convertToCSV(file);
            }
            try (BufferedReader answersReader = new BufferedReader(new FileReader(file))) {
                answerDataByQuizMasterAndParticipant.parseAnswers(answersReader, solutionsByQuizMaster);
            } catch (RuntimeException | IOException e) {
                throw new IOException("Error in file " + file.getAbsolutePath(), e);
            }
        }
        return new ResultsByQuizMasterAndParticipant(
            answerDataByQuizMasterAndParticipant,
            Main.selectResultComputation(mode)
        );
    }

    private static List<String> parseCanceled(final Optional<File> file) throws IOException {
        if (file.isEmpty()) {
            return Collections.emptyList();
        }
        return Files
            .lines(file.get().toPath())
            .filter(line -> !line.isBlank())
            .toList();
    }

    private static Map<String, Integer> parseExcuses(final Optional<File> file) throws IOException {
        if (file.isEmpty()) {
            return Collections.emptyMap();
        }
        return Files
            .lines(file.get().toPath())
            .filter(line -> !line.isBlank())
            .map(line -> line.split(";"))
            .collect(Collectors.toMap(split -> split[0], split -> Integer.parseInt(split[1])));
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

    private static void updateProtocols(
        final Parameters<Flag> options,
        final ResultsByQuizMasterAndParticipant results,
        final List<Evaluation> quizMasterEvaluation,
        final List<Evaluation> participantsEvaluation,
        final Map<String, Integer> excused,
        final List<String> canceled
    ) throws IOException {
        final File[] protocols =
            new File(options.get(Flag.SOLUTIONS))
            .getAbsoluteFile()
            .toPath()
            .getParent()
            .resolve("protocols")
            .toFile()
            .listFiles();
        for (final File protocol : protocols) {
            if (protocol.getName().endsWith(".tex")) {
                new ProtocolUpdater(
                    protocol.toPath(),
                    results,
                    quizMasterEvaluation,
                    participantsEvaluation
                ).updateProtocol(excused, canceled);
            }
        }
    }

}
