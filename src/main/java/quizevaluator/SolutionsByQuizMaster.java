package quizevaluator;

import java.io.*;
import java.util.*;

public class SolutionsByQuizMaster extends LinkedHashMap<String, Answer> {

    private static final long serialVersionUID = -1349168626196465688L;

    public SolutionsByQuizMaster(final BufferedReader reader) throws IOException {
        String line  = reader.readLine();
        while (line != null) {
            if (line.isBlank()) {
                line = reader.readLine();
                continue;
            }
            final int firstSemicolon = line.indexOf(';');
            final String quizMaster = Main.toASCII(line.substring(0, firstSemicolon));
            final String solutionString = line.substring(firstSemicolon + 1);
            if (solutionString.isBlank()) {
                this.put(quizMaster, new Answer());
                line = reader.readLine();
                continue;
            }
            final int[] solutionArray =
                Arrays.stream(solutionString.split(";")).mapToInt(AnswerParser.INSTANCE).toArray();
            final Answer solution = new Answer();
            for (int i = 0; i < solutionArray.length; i++) {
                solution.put(i + 1, solutionArray[i]);
            }
            this.put(quizMaster, solution);
            line = reader.readLine();
        }
    }

    public SolutionsByQuizMaster(final Map<String, Answer> map) {
        super(map);
    }

}
