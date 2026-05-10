package iso15939.model;

public class Metric extends MeasurementElement {
    private final Direction direction;
    private final double minimum;
    private final double maximum;
    private final String unit;
    private final double value;

    public Metric(String name, double coefficient, Direction direction, double minimum, double maximum,
                  String unit, double value) {
        super(name, coefficient);
        this.direction = direction;
        this.minimum = minimum;
        this.maximum = maximum;
        this.unit = unit;
        this.value = value;
    }

    public Direction getDirection() {
        return direction;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getMaximum() {
        return maximum;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    public String getRangeLabel() {
        return formatNumber(minimum) + "-" + formatNumber(maximum);
    }

    @Override
    public String getDisplayLabel() {
        return getName() + " (" + formatNumber(getCoefficient()) + " / " + unit + ")";
    }

    private String formatNumber(double number) {
        if (number == Math.rint(number)) {
            return String.valueOf((int) number);
        }
        return String.format("%.1f", number);
    }
}
