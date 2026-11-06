package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.report.*;
import java.util.ArrayList;
import java.util.List;

public final class SecurityAuditVisitor extends RecursiveProjectVisitor {
    private final List<InspectionIssue> issues = new ArrayList<>();

    @Override
    public void visit(SourceCodeFile file) {
        if (file.hardcodedCredentials()) {
            add(Severity.CRITICAL, "HARDCODED_CREDENTIALS", file.name(), "Move credentials to a secret store.");
        }
        if (file.highRisk()) {
            add(Severity.HIGH, "HIGH_RISK_SOURCE", file.name(), "Review sensitive operations before release.");
        }
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.exposedCredentials()) {
            add(Severity.CRITICAL, "EXPOSED_CREDENTIALS", file.name(), "Remove exposed configuration credentials.");
        }
        if (!file.secureTransportEnabled()) {
            add(Severity.HIGH, "INSECURE_TRANSPORT", file.name(), "Enable encrypted transport.");
        }
        if (file.debugEnabled()) {
            add(Severity.MEDIUM, "DEBUG_ENABLED", file.name(), "Disable debug mode for release.");
        }
    }

    @Override
    public void visit(DependencyFile file) {
        for (Dependency dependency : file.dependencies()) {
            if (dependency.vulnerable()) {
                add(Severity.CRITICAL, "VULNERABLE_DEPENDENCY", file.name(),
                        "Upgrade " + dependency.coordinate() + ":" + dependency.version() + ".");
            }
        }
    }

    public SecurityReport report() { return new SecurityReport(issues); }

    private void add(Severity severity, String rule, String element, String message) {
        issues.add(new InspectionIssue(severity, rule, element, message));
    }
}
