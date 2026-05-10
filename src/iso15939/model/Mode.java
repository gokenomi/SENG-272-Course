package iso15939.model;

public enum Mode {
    CUSTOM("Custom"),
    HEALTH("Health"),
    EDUCATION("Education");

    private final String displayName;

    Mode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
