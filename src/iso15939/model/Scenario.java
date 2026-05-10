package iso15939.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scenario {
    private final String name;
    private final QualityType qualityType;
    private final Mode mode;
    private final String description;
    private final List<Dimension> dimensions;

    public Scenario(String name, QualityType qualityType, Mode mode, String description, List<Dimension> dimensions) {
        this.name = name;
        this.qualityType = qualityType;
        this.mode = mode;
        this.description = description;
        this.dimensions = new ArrayList<>(dimensions);
    }

    public String getName() {
        return name;
    }

    public QualityType getQualityType() {
        return qualityType;
    }

    public Mode getMode() {
        return mode;
    }

    public String getDescription() {
        return description;
    }

    public List<Dimension> getDimensions() {
        return Collections.unmodifiableList(dimensions);
    }

    @Override
    public String toString() {
        return name;
    }
}
