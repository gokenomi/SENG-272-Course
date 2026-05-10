package iso15939.model;

public class DimensionResult {
    private final Dimension dimension;
    private final double score;

    public DimensionResult(Dimension dimension, double score) {
        this.dimension = dimension;
        this.score = score;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public double getScore() {
        return score;
    }
}
