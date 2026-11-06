package edu.assignment.inspection.report;

import java.util.List;

public record SecurityReport(List<InspectionIssue> issues) {
    public SecurityReport { issues = List.copyOf(issues); }

    public long criticalIssues() {
        return issues.stream().filter(issue -> issue.severity() == Severity.CRITICAL).count();
    }

    public int score() {
        long penalty = issues.stream().mapToLong(issue -> issue.severity().penalty()).sum();
        return (int) Math.max(0, 100 - penalty);
    }
}
