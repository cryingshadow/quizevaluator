package quizevaluator;

import com.google.common.collect.Sets;

import java.io.*;
import java.util.HashSet;

public class Main {

    public static void main(final String[] args) throws IOException {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException(
                "You must provide a file with correct answers, a directory with files containing given answers, and a file for the output!"
            );
        }
        final SolutionsByQuizMaster solutionsByQuiz = Main.parseSolutions(args[0]);
        final AnswersByQuizMasterAndParticipant answersByQuiz = new AnswersByQuizMasterAndParticipant();
        boolean thereWereErrors = false;
        for (final File file : new File(args[1]).listFiles()) {
            try (BufferedReader answersReader = new BufferedReader(new FileReader(file))) {
                answersByQuiz.parseAnswers(answersReader, file.getName());
            }
            catch (Exception e) {
                thereWereErrors = true;
                System.err.println("File " + file.getName() + ": " + e.getMessage());
            }
        }
        if (thereWereErrors) {
            System.err.println("\nErrors while parsing answers - please fix them. Exiting...");
            System.exit(1);
        }

        plausibilityCheck(solutionsByQuiz, answersByQuiz);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(args[2]))) {
            new ResultsByQuizMasterAndParticipant(solutionsByQuiz, answersByQuiz).output(writer);
        } catch (Exception e) {
            System.err.println("Error while computing results: " + e.getMessage());
            System.exit(1);
        }
    }

    private static SolutionsByQuizMaster parseSolutions(final String file) throws IOException {
        try (BufferedReader solutionReader = new BufferedReader(new FileReader(file))) {
            return new SolutionsByQuizMaster(solutionReader);
        }
    }

    private static void plausibilityCheck(SolutionsByQuizMaster solutionsByQuiz, AnswersByQuizMasterAndParticipant answersByQuiz) {
        System.out.println("Plausibility check\n==================");
        System.out.println("Nr of quizmasters: " + solutionsByQuiz.size());

        var allParticipants = new HashSet<String>();
        for (var answers : answersByQuiz.values() ) {
            allParticipants.addAll(answers.keySet());
        }

        var quizmastersNotInParticipants = Sets.difference(solutionsByQuiz.keySet(), allParticipants);
        var participantsNotInQuizmasters = Sets.difference(allParticipants, solutionsByQuiz.keySet());

        if (quizmastersNotInParticipants.isEmpty() && participantsNotInQuizmasters.isEmpty()) {
            System.out.println("No problems found.");
        } else {
            System.out.println("Quizmasters not in participants: " + quizmastersNotInParticipants);
            System.out.println("Participants not in quizmasters: " + participantsNotInQuizmasters);
        }
    }

}
