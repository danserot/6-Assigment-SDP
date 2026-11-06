package edu.assignment.inspection.demo;

import edu.assignment.inspection.report.*;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Objects;

public final class ConsoleReporter {
    private final PrintStream out;

    public ConsoleReporter(PrintStream out) { this.out = Objects.requireNonNull(out, "out"); }

    public void security(SecurityReport report) {
        out.printf("%nSECURITY AUDIT%nScore: %d/100 | Critical issues: %d%n",
                report.score(), report.criticalIssues());
        report.issues().forEach(this::issue);
        if (report.issues().isEmpty()) { out.println("No security issues detected."); }
    }

    public void statistics(ProjectStatistics report) {
        out.printf(Locale.ROOT,
                "%nPROJECT STATISTICS%nFiles: %d | Source files: %d | Test files: %d%n"
                + "Lines of code: %d | Test methods: %d | Dependencies: %d%n"
                + "Average complexity: %.2f | Test/source file ratio: %.2f%n",
                report.totalFiles(), report.sourceFiles(), report.testFiles(), report.linesOfCode(),
                report.numberOfTests(), report.dependencies(), report.averageComplexity(), report.testSourceRatio());
    }

    public void quality(QualityReport report) {
        out.printf(Locale.ROOT, "%nQUALITY GATE (complexity <= %.2f, tests/source >= %.2f)%n",
                report.policy().maxAverageComplexity(), report.policy().minTestSourceRatio());
        out.println("Source project: " + status(report.hasSources()));
        out.println("Security: " + status(report.securityPassed()));
        out.println("Complexity: " + status(report.complexityPassed()));
        out.println("Tests: " + status(report.testsPassed()));
        out.println("FINAL RESULT: " + (report.passed() ? "PASSED" : "FAILED"));
    }

    public void compliance(ComplianceReport report) {
        out.printf("%nCOMPLIANCE: %s | Violations: %d%n", status(report.passed()), report.violations().size());
        report.violations().forEach(this::issue);
    }

    public void technicalDebt(TechnicalDebtReport report) {
        out.printf("%nTECHNICAL DEBT%nEstimated work: %d minutes (heuristic)%n", report.totalMinutes());
        report.items().forEach(item -> out.printf("%s | %s | %d min%n",
                item.element(), item.reason(), item.estimatedMinutes()));
    }

    private void issue(InspectionIssue issue) {
        out.printf("[%s] %s | %s | %s%n", issue.severity(), issue.rule(), issue.element(), issue.message());
    }

    private static String status(boolean passed) { return passed ? "PASS" : "FAIL"; }
}
