package iso15939.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dimension extends MeasurementElement {
    private final List<Metric> metrics;

    public Dimension(String name, double coefficient, List<Metric> metrics) {
        super(name, coefficient);
        this.metrics = new ArrayList<>(metrics);
    }

    public List<Metric> getMetrics() {
        return Collections.unmodifiableList(metrics);
    }

    @Override
    public String getDisplayLabel() {
        return getName() + " (" + (int) getCoefficient() + ")";
    }
}
