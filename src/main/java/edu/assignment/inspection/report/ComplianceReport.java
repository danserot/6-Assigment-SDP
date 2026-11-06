package edu.assignment.inspection.report;

import java.util.List;

public record ComplianceReport(List<InspectionIssue> violations) {
    public ComplianceReport { violations = List.copyOf(violations); }
    public boolean passed() { return violations.isEmpty(); }
}
