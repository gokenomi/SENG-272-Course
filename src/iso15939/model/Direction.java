package iso15939.model;

public enum Direction {
    HIGHER_BETTER("Higher ↑"),
    LOWER_BETTER("Lower ↓");

    private final String displayName;

    Direction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
