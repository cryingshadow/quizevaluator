package quizevaluator;

public enum ExecutionMode {

    FULL, NEW, OLD;

    public static ExecutionMode from(final String name) {
        final String lowerCase = name.trim().toLowerCase();
        for (final ExecutionMode mode : ExecutionMode.values()) {
            if (mode.name().toLowerCase().equals(lowerCase)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unknown execution mode!");
    }

}
