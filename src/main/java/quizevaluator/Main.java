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

    private static final Collection<Set<Flag>> ALLOWED_COMBINATIONS =
        List.of(
            Set.of(Flag.SOLUTIONS, Flag.ANSWERS, Flag.OUTPUT),
            Set.of(Flag.SOLUTIONS, Flag.ANSWERS, Flag.MODE, Flag.OUTPUT),
            Set.of(Flag.SOLUTIONS, Flag.ANSWERS, Flag.EXCUSES, Flag.OUTPUT),
            Set.of(Flag.SOLUTIONS, Flag.ANSWERS, Flag.EXCUSES, Flag.MODE, Flag.OUTPUT)
        );

    public static void main(final String[] args)
    throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final CLITamer<Flag> tamer = new CLITamer<Flag>(Flag.class);
        if (args == null || args.length < 1) {
            System.out.println(tamer.getParameterDescriptions());
            System.out.println(Main.helpText());
            return;
        }
        final Parameters<Flag> options = tamer.parse(args);
        if (!Main.ALLOWED_COMBINATIONS.contains(options.keySet())) {
            System.out.println(tamer.getParameterDescriptions());
            System.out.println(Main.helpText());
            return;
        }
        final FormsExcelToCsvConverter excelConverter = new FormsExcelToCsvConverter();
        final SolutionsByQuizMaster solutionsByQuizMaster = Main.parseSolutions(options.get(Flag.SOLUTIONS));
        final AnswerDataByQuizMasterAndParticipant answerDataByQuizMasterAndParticipant =
            new AnswerDataByQuizMasterAndParticipant();
        for (File file : new File(options.get(Flag.ANSWERS)).listFiles()) {
            if (excelConverter.isExcelFile(file)) {
                file = excelConverter.convert(file);
            }
            try (BufferedReader answersReader = new BufferedReader(new FileReader(file))) {
                answerDataByQuizMasterAndParticipant.parseAnswers(answersReader, solutionsByQuizMaster);
            } catch (RuntimeException | IOException e) {
                throw new IOException("Error in file " + file.getAbsolutePath(), e);
            }
        }
        final ExecutionMode mode = ExecutionMode.from(options.getOrDefault(Flag.MODE, "FULL"));
        final ResultsByQuizMasterAndParticipant results =
            new ResultsByQuizMasterAndParticipant(
                answerDataByQuizMasterAndParticipant,
                Main.selectResultComputation(mode)
            );
        final Map<String, Integer> excused =
            Main.parseExcuses(Optional.ofNullable(options.get(Flag.EXCUSES)).map(File::new));
        final List<Evaluation> quizMasterEvaluation = Main.selectQuizMasterEvaluation(mode);
        final List<Evaluation> participantsEvaluation = Main.selectParticipantsEvaluation(mode);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(options.get(Flag.OUTPUT)))) {
            new CSVWriter(writer).writeCSV(results, quizMasterEvaluation, participantsEvaluation, excused);
        }
        if (mode == ExecutionMode.FULL) {
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
                    ).updateProtocol(excused);
                }
            }
        }
    }

    private static String helpText() {
        return String.format(
            "Allowed combinations: %s",
            Main.ALLOWED_COMBINATIONS
            .stream()
            .map(set ->
                set
                .stream()
                .map(flag -> "-" + flag.shortName())
                .collect(Collectors.joining(" and "))
            ).collect(Collectors.joining(", "))
        );
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

}
