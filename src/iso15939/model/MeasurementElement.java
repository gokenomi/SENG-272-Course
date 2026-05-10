package iso15939.model;

public abstract class MeasurementElement {
    private final String name;
    private final double coefficient;

    protected MeasurementElement(String name, double coefficient) {
        this.name = name;
        this.coefficient = coefficient;
    }

    public String getName() {
        return name;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public abstract String getDisplayLabel();
}
