package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.SourceCodeFile;
import edu.assignment.inspection.model.TestFile;
import edu.assignment.inspection.report.TechnicalDebtItem;
import edu.assignment.inspection.report.TechnicalDebtReport;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Heuristic planning estimates, not measurements of actual engineering time. */
public final class TechnicalDebtVisitor extends RecursiveProjectVisitor {
    private final List<TechnicalDebtItem> items = new ArrayList<>();
    private final Set<String> productionModules = new LinkedHashSet<>();
    private final Set<String> testedModules = new LinkedHashSet<>();

    @Override
    public void visit(SourceCodeFile file) {
        if (file.production()) { productionModules.add(file.module()); }
        long excessComplexity = Math.max(0L, (long) file.complexity() - 10);
        if (excessComplexity > 0) {
            items.add(new TechnicalDebtItem(file.name(), "Reduce complexity above 10", excessComplexity * 30));
        }
        if (file.hardcodedCredentials()) {
            items.add(new TechnicalDebtItem(file.name(), "Remove hardcoded credentials", 60));
        }
        if (file.highRisk()) {
            items.add(new TechnicalDebtItem(file.name(), "Review high-risk source", 45));
        }
    }

    @Override
    public void visit(TestFile file) {
        if (file.testCount() > 0) { testedModules.add(file.module()); }
    }

    public TechnicalDebtReport report() {
        List<TechnicalDebtItem> result = new ArrayList<>(items);
        for (String module : productionModules) {
            if (!testedModules.contains(module)) {
                result.add(new TechnicalDebtItem(module, "Add tests for a production module", 120));
            }
        }
        return new TechnicalDebtReport(result);
    }
}
