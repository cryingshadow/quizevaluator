package quizevaluator;

import java.io.*;
import java.util.*;

public class AnswersByQuizMasterAndParticipant extends LinkedHashMap<String, Map<String, Map<Integer, Integer>>> {

    private static final long serialVersionUID = 5684072421495378521L;

    private static int parseNumber(final String answer) {
        return Integer.parseInt(
            answer
            .codePoints()
            .filter(Character::isDigit)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString()
        );
    }

    public AnswersByQuizMasterAndParticipant() {
        super();
    }

    public AnswersByQuizMasterAndParticipant(final Map<String, Map<String, Map<Integer, Integer>>> map) {
        super(map);
    }

    public void parseAnswers(final BufferedReader reader, final String file) throws IOException {
        final String key = reader.readLine().trim();
        final Map<String, Map<Integer, Integer>> answersByTest = new LinkedHashMap<String, Map<Integer, Integer>>();
        String line = reader.readLine();
        while (line != null) {
            if (line.isBlank()) {
                line = reader.readLine();
                continue;
            }
            final String[] nameAndAnswers = line.split(";");
            if (nameAndAnswers.length < 2) {
                System.out.println("Warning for file " + file + ": Line does not have semicolon. Ignoring... Line:\n" + line);
                line = reader.readLine();
                continue;
            }
            if (nameAndAnswers.length > 2) {
                throw new IOException("Format not ok - more than one semicolon in a line! Line:\n" + line);
            }
            final Map<Integer, Integer> answers = new LinkedHashMap<Integer, Integer>();
            for (final String answer : nameAndAnswers[1].split(",")) {
                final Integer value = AnswerParser.INSTANCE.apply(answer);
                if (value != null) {
                    answers.put(AnswersByQuizMasterAndParticipant.parseNumber(answer), value);
                }
            }
            answersByTest.put(nameAndAnswers[0], answers);
            line = reader.readLine();
        }
        this.put(key, answersByTest);
    }

}
