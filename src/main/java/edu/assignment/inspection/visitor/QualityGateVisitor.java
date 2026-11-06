package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.report.*;
import java.util.Objects;

/** Reuses the same rules as standalone audits, with one tree traversal. */
public final class QualityGateVisitor extends RecursiveProjectVisitor {
    private final SecurityAuditVisitor security = new SecurityAuditVisitor();
    private final ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor();
    private final QualityPolicy policy;

    public QualityGateVisitor() { this(QualityPolicy.releaseDefaults()); }

    public QualityGateVisitor(QualityPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy");
    }

    @Override
    public void visit(SourceCodeFile file) { inspect(file); }

    @Override
    public void visit(TestFile file) { inspect(file); }

    @Override
    public void visit(ConfigurationFile file) { inspect(file); }

    @Override
    public void visit(DependencyFile file) { inspect(file); }

    public QualityReport report() {
        var stats = statistics.report();
        long critical = security.report().criticalIssues();
        return new QualityReport(stats.sourceFiles() > 0, critical == 0,
                stats.averageComplexity() <= policy.maxAverageComplexity(),
                stats.sourceFiles() > 0 && stats.testSourceRatio() >= policy.minTestSourceRatio(),
                stats, critical, policy);
    }

    private void inspect(ProjectElement element) {
        element.accept(security);
        element.accept(statistics);
    }
}
