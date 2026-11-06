package edu.assignment.inspection.report;

public record QualityPolicy(double maxAverageComplexity, double minTestSourceRatio) {
    public QualityPolicy {
        if (!Double.isFinite(maxAverageComplexity) || maxAverageComplexity < 0
                || !Double.isFinite(minTestSourceRatio) || minTestSourceRatio < 0) {
            throw new IllegalArgumentException("Quality thresholds must be finite and non-negative");
        }
    }

    public static QualityPolicy releaseDefaults() { return new QualityPolicy(10, 0.40); }
}
