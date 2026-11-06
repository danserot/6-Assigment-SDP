package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TraversalEdgeCaseTest {
    @Test
    void allVisitorsHandleAProjectNested256LevelsDeep() {
        ProjectElement root = Fixtures.source("deep", 9);
        for (int i = 0; i < 256; i++) { root = new Directory("level" + i, List.of(root)); }
        var statistics = new ProjectStatisticsVisitor();
        var security = new SecurityAuditVisitor();
        var quality = new QualityGateVisitor();
        var compliance = new ComplianceVisitor();
        var debt = new TechnicalDebtVisitor();
        root.accept(statistics);
        root.accept(security);
        root.accept(quality);
        root.accept(compliance);
        root.accept(debt);
        assertEquals(1, statistics.report().totalFiles());
        assertEquals(100, statistics.report().linesOfCode());
        assertEquals(100, security.report().score());
        assertFalse(quality.report().testsPassed());
        assertEquals(1, compliance.report().violations().size());
        assertEquals(120, debt.report().totalMinutes());
    }

    @Test
    void dependencyFileOwnsAnImmutableCopyOfItsDependencies() {
        var dependencies = new ArrayList<>(List.of(new Dependency("a:b", "1", false)));
        var file = new DependencyFile("pom.xml", 10, dependencies);
        dependencies.clear();
        assertEquals(1, file.dependencies().size());
        assertThrows(UnsupportedOperationException.class, () -> file.dependencies().clear());
    }

    @Test
    void highRiskWarningLowersTheScoreButDoesNotCountAsCritical() {
        var root = new Directory("root", List.of(
                new SourceCodeFile("Risk.java", "risk", true, 10, 5, false, true),
                Fixtures.tests("risk")));
        var security = new SecurityAuditVisitor();
        var quality = new QualityGateVisitor();
        root.accept(security);
        root.accept(quality);
        assertEquals(85, security.report().score());
        assertEquals(0, security.report().criticalIssues());
        assertTrue(quality.report().passed());
    }

    @Test
    void ratioBelowThresholdFailsEvenWhenSomeTestsExist() {
        List<ProjectElement> children = new ArrayList<>();
        for (int i = 0; i < 5; i++) { children.add(Fixtures.source("m" + i, 5)); }
        children.add(Fixtures.tests("m0"));
        var quality = new QualityGateVisitor();
        new Directory("root", children).accept(quality);
        assertEquals(0.2, quality.report().statistics().testSourceRatio());
        assertTrue(quality.report().securityPassed());
        assertTrue(quality.report().complexityPassed());
        assertFalse(quality.report().testsPassed());
        assertFalse(quality.report().passed());
    }
}
