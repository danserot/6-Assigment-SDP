package edu.assignment.inspection.report;

public record ProjectStatistics(long totalFiles, long sourceFiles, long testFiles,
        long linesOfCode, long numberOfTests, long dependencies, long totalComplexity) {
    public double averageComplexity() {
        return sourceFiles == 0 ? 0.0 : (double) totalComplexity / sourceFiles;
    }

    /** File ratio, not line coverage or the number of test methods. */
    public double testSourceRatio() {
        return sourceFiles == 0 ? 0.0 : (double) testFiles / sourceFiles;
    }
}
