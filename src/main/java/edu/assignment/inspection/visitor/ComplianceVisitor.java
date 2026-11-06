package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.report.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class ComplianceVisitor extends RecursiveProjectVisitor {
    private final Set<String> prohibitedDependencies;
    private final Set<String> productionModules = new LinkedHashSet<>();
    private final Set<String> testedModules = new LinkedHashSet<>();
    private final List<InspectionIssue> violations = new ArrayList<>();

    public ComplianceVisitor() { this(Set.of("com.legacy:unsafe-crypto")); }

    public ComplianceVisitor(Set<String> prohibitedDependencies) {
        this.prohibitedDependencies = Set.copyOf(prohibitedDependencies);
    }

    @Override
    public void visit(SourceCodeFile file) {
        if (file.production()) { productionModules.add(file.module()); }
        if (file.hardcodedCredentials()) { exposedCredentials(file.name()); }
    }

    @Override
    public void visit(TestFile file) {
        if (file.testCount() > 0) { testedModules.add(file.module()); }
    }

    @Override
    public void visit(ConfigurationFile file) {
        if (file.exposedCredentials()) { exposedCredentials(file.name()); }
    }

    @Override
    public void visit(DependencyFile file) {
        for (Dependency dependency : file.dependencies()) {
            if (dependency.version().isBlank()) {
                violations.add(new InspectionIssue(Severity.HIGH, "MISSING_VERSION", file.name(),
                        "Specify a version for " + dependency.coordinate() + "."));
            }
            if (prohibitedDependencies.contains(dependency.coordinate())) {
                violations.add(new InspectionIssue(Severity.HIGH, "PROHIBITED_DEPENDENCY", file.name(),
                        "Remove prohibited dependency " + dependency.coordinate() + "."));
            }
        }
    }

    /** Evaluate cross-file rules after traversal so file order cannot change the result. */
    public ComplianceReport report() {
        List<InspectionIssue> result = new ArrayList<>(violations);
        for (String module : productionModules) {
            if (!testedModules.contains(module)) {
                result.add(new InspectionIssue(Severity.HIGH, "UNTESTED_PRODUCTION_MODULE", module,
                        "Add at least one test to production module " + module + "."));
            }
        }
        return new ComplianceReport(result);
    }

    private void exposedCredentials(String name) {
        violations.add(new InspectionIssue(Severity.CRITICAL, "EXPOSED_CREDENTIALS", name,
                "Credentials must not be exposed in project files."));
    }
}
