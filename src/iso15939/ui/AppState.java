package iso15939.ui;

import iso15939.model.Mode;
import iso15939.model.Profile;
import iso15939.model.QualityType;
import iso15939.model.Scenario;

public class AppState {
    private final Profile profile = new Profile();
    private QualityType qualityType = QualityType.PRODUCT;
    private Mode mode = Mode.EDUCATION;
    private Scenario scenario;

    public Profile getProfile() {
        return profile;
    }

    public QualityType getQualityType() {
        return qualityType;
    }

    public void setQualityType(QualityType qualityType) {
        this.qualityType = qualityType;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }
}
