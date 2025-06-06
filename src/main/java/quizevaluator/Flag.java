package quizevaluator;

import clit.*;

public enum Flag implements Parameter {

    ANSWERS("a", "answers", "Folder containing the quiz participants' answers."),

    EXCUSES("e", "excuses", "File containing the number of excused quizzes for each participant."),

    MODE("m", "mode", "Execution mode (FULL, NEW, or OLD)."),

    OUTPUT("o", "output", "File for output."),

    SOLUTIONS("s", "solutions", "File containing the correct solutions fpr the quizzes.");

    private final String description;

    private final String longName;

    private final String shortName;

    private Flag(final String shortName, final String longName, final String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String longName() {
        return this.longName;
    }

    @Override
    public String shortName() {
        return this.shortName;
    }

}
