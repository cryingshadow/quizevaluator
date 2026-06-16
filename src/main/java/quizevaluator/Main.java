package quizevaluator;

import java.io.*;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

import clit.*;
import quizevaluator.evaluations.*;

public class Main {

    static final Evaluations EVALUATIONS_NEW =
        new Evaluations(
            new QuizMasterEvaluations(
                new TotalPointsForQuizMasterEvaluation(),
                new PointsPercentageForQuizMasterEvaluation(),
                new Passed6CountForQuizMasterEvaluation(),
                new Passed6PercentageForQuizMasterEvaluation(),
                new Passed6TotalForQuizMasterEvaluation(),
                new Passed9CountForQuizMasterEvaluation(),
                new Passed9PercentageForQuizMasterEvaluation(),
                new ModernBonusForQuizMasterEvaluation()
            ),
            new ParticipantEvaluations(
                new TotalPointsForParticipantEvaluation(),
                new PointsPercentageForParticipantEvaluation(),
                new Passed6CountForParticipantEvaluation(),
                new Passed6PercentageForParticipantEvaluation(),
                new Passed9CountForParticipantEvaluation(),
                new Passed9PercentageForParticipantEvaluation(),
                new ModernBonusForParticipantEvaluation()
            )
        );

    static final Evaluations EVALUATIONS_OLD =
        new Evaluations(
            new QuizMasterEvaluations(
                new TotalPointsForQuizMasterEvaluation(),
                new PointsPercentageForQuizMasterEvaluation(),
                new Passed5CountForQuizMasterEvaluation(),
                new Passed5PercentageForQuizMasterEvaluation(),
                new Passed5TotalForQuizMasterEvaluation(),
                new Passed8CountForQuizMasterEvaluation(),
                new Passed8PercentageForQuizMasterEvaluation(),
                new BonusForQuizMasterEvaluation()
            ),
            new ParticipantEvaluations(
                new TotalPointsForParticipantEvaluation(),
                new PointsPercentageForParticipantEvaluation(),
                new Passed5CountForParticipantEvaluation(),
                new Passed5PercentageForParticipantEvaluation(),
                new Passed8CountForParticipantEvaluation(),
                new Passed8PercentageForParticipantEvaluation(),
                new BonusForParticipantEvaluation()
            )
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
        final Evaluations evaluations = Main.selectEvaluations(mode);
        final List<String> canceled =
            Main.parseCanceled(Optional.ofNullable(options.get(Flag.CANCELED)).map(File::new));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(options.get(Flag.OUTPUT)))) {
            new CSVWriter(writer).writeCSV(results, evaluations, excused, canceled);
        }
        if (mode == ExecutionMode.FULL) {
            Main.updateProtocols(options, results, evaluations, excused, canceled);
        }
    }

    public static String toASCII(final String name) {
        return name
            .replaceAll("ä", "ae")
            .replaceAll("Ä", "Ae")
            .replaceAll("ö", "oe")
            .replaceAll("Ö", "Oe")
            .replaceAll("ü", "ue")
            .replaceAll("Ü", "Ue")
            .replaceAll("ß", "ss")
            .replaceAll("Á", "A")
            .replaceAll("á", "a")
            .replaceAll("É", "E")
            .replaceAll("é", "e")
            .replaceAll("[^\\x00-\\x7F]", "");
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
            solutionsByQuizMaster,
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
            .map(Main::toASCII)
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
            .collect(Collectors.toMap(split -> Main.toASCII(split[0]), split -> Integer.parseInt(split[1])));
    }

    private static SolutionsByQuizMaster parseSolutions(final String file) throws IOException {
        try (BufferedReader solutionReader = new BufferedReader(new FileReader(file))) {
            return new SolutionsByQuizMaster(solutionReader);
        }
    }

    private static Evaluations selectEvaluations(final ExecutionMode mode) {
        switch (mode) {
        case OLD:
            return Main.EVALUATIONS_OLD;
        case NEW:
        case FULL:
            return Main.EVALUATIONS_NEW;
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
        final Evaluations evaluations,
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
                    evaluations
                ).updateProtocol(excused, canceled);
            }
        }
    }

}
