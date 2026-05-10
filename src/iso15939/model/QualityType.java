package iso15939.model;

public enum QualityType {
    PRODUCT("Product Quality"),
    PROCESS("Process Quality");

    private final String displayName;

    QualityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
