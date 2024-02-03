package quizevaluator;

import java.io.*;
import java.util.*;

public class SolutionsByQuizMaster extends LinkedHashMap<String, Map<Integer, Integer>> {

    private static final long serialVersionUID = -1349168626196465688L;

    public SolutionsByQuizMaster(final BufferedReader reader) throws IOException {
        String line  = reader.readLine();
        while (line != null) {
            if (line.isBlank()) {
                line = reader.readLine();
                continue;
            }
            final int firstSemicolon = line.indexOf(';');
            final String quizMaster = line.substring(0, firstSemicolon);
            final String solutionString = line.substring(firstSemicolon + 1);
            if (solutionString.isBlank()) {
                line = reader.readLine();
                continue;
            }
            final int[] solutionArray =
                Arrays.stream(solutionString.split(";")).mapToInt(AnswerParser.INSTANCE).toArray();
            if (solutionArray.length != 10) {
                throw new IOException("Quizmaster " + quizMaster + ": Solution contains " + solutionArray.length + " answers!");
            }

            final Map<Integer, Integer> solution = new LinkedHashMap<Integer, Integer>();
            for (int i = 0; i < solutionArray.length; i++) {
                solution.put(i + 1, solutionArray[i]);
            }
            this.put(quizMaster, solution);
            line = reader.readLine();
        }
    }

    public SolutionsByQuizMaster(final Map<String, Map<Integer, Integer>> map) {
        super(map);
    }

}
