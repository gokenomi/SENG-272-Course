package iso15939.service;

import iso15939.model.Dimension;
import iso15939.model.DimensionResult;
import iso15939.model.Direction;
import iso15939.model.Metric;
import iso15939.model.Scenario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MeasurementService {
    public double calculateMetricScore(Metric metric) {
        double range = metric.getMaximum() - metric.getMinimum();
        if (range <= 0) {
            return 1.0;
        }

        double normalized = (metric.getValue() - metric.getMinimum()) / range;
        double score;
        if (metric.getDirection() == Direction.HIGHER_BETTER) {
            score = 1 + normalized * 4;
        } else {
            score = 5 - normalized * 4;
        }
        return roundToNearestHalf(clamp(score, 1.0, 5.0));
    }

    public double calculateDimensionScore(Dimension dimension) {
        double weightedTotal = 0.0;
        double coefficientTotal = 0.0;
        for (Metric metric : dimension.getMetrics()) {
            weightedTotal += calculateMetricScore(metric) * metric.getCoefficient();
            coefficientTotal += metric.getCoefficient();
        }
        if (coefficientTotal == 0) {
            return 0.0;
        }
        return roundToNearestHalf(weightedTotal / coefficientTotal);
    }

    public List<DimensionResult> calculateDimensionResults(Scenario scenario) {
        List<DimensionResult> results = new ArrayList<>();
        for (Dimension dimension : scenario.getDimensions()) {
            results.add(new DimensionResult(dimension, calculateDimensionScore(dimension)));
        }
        return results;
    }

    public DimensionResult findLowestResult(List<DimensionResult> results) {
        return results.stream()
                .min(Comparator.comparingDouble(DimensionResult::getScore))
                .orElse(null);
    }

    public String getQualityLabel(double score) {
        if (score >= 4.5) {
            return "Excellent";
        }
        if (score >= 3.5) {
            return "Good";
        }
        if (score >= 2.5) {
            return "Needs Improvement";
        }
        return "Poor";
    }

    private double roundToNearestHalf(double value) {
        return Math.round(value * 2.0) / 2.0;
    }

    private double clamp(double value, double minimum, double maximum) {
        return Math.max(minimum, Math.min(maximum, value));
    }
}
