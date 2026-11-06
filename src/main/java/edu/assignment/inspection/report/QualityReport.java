package edu.assignment.inspection.report;

public record QualityReport(boolean hasSources, boolean securityPassed,
        boolean complexityPassed, boolean testsPassed, ProjectStatistics statistics,
        long criticalIssues, QualityPolicy policy) {
    public boolean passed() {
        return hasSources && securityPassed && complexityPassed && testsPassed;
    }
}
