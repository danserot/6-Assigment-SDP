package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.report.QualityPolicy;
import edu.assignment.inspection.visitor.QualityGateVisitor;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QualityGateVisitorTest {
    @Test
    void cleanProjectPassesAllGates() {
        var visitor = new QualityGateVisitor();
        Fixtures.cleanProject().accept(visitor);
        assertTrue(visitor.report().passed());
        assertEquals(6, visitor.report().statistics().totalFiles());
    }

    @Test
    void criticalSecurityIssueBlocksAnOtherwiseCleanRelease() {
        var visitor = new QualityGateVisitor();
        new Directory("root", List.of(Fixtures.cleanProject(),
                new ConfigurationFile("secrets", 1, true, true, false))).accept(visitor);
        assertFalse(visitor.report().passed());
        assertFalse(visitor.report().securityPassed());
        assertTrue(visitor.report().complexityPassed());
        assertTrue(visitor.report().testsPassed());
    }

    @Test
    void highComplexityFailsIndependently() {
        var visitor = new QualityGateVisitor();
        new Directory("root", List.of(Fixtures.source("a", 11), Fixtures.tests("a"))).accept(visitor);
        assertFalse(visitor.report().complexityPassed());
        assertTrue(visitor.report().securityPassed());
        assertFalse(visitor.report().passed());
    }

    @Test
    void missingTestsFailsTheTestGate() {
        var visitor = new QualityGateVisitor();
        Fixtures.source("a", 5).accept(visitor);
        assertFalse(visitor.report().testsPassed());
        assertFalse(visitor.report().passed());
    }

    @Test
    void exactThresholdsPass() {
        List<ProjectElement> elements = new ArrayList<>();
        for (int i = 0; i < 5; i++) { elements.add(Fixtures.source("m" + i, 10)); }
        elements.add(Fixtures.tests("m0"));
        elements.add(Fixtures.tests("m1"));
        var visitor = new QualityGateVisitor();
        new Directory("root", elements).accept(visitor);
        assertEquals(0.40, visitor.report().statistics().testSourceRatio());
        assertTrue(visitor.report().passed());
    }

    @Test
    void emptyProjectCannotBeReleased() {
        var visitor = new QualityGateVisitor();
        new Directory("empty", List.of()).accept(visitor);
        assertFalse(visitor.report().hasSources());
        assertFalse(visitor.report().passed());
    }

    @Test
    void customPolicyChangesTheDecisionAndRejectsInvalidThresholds() {
        var visitor = new QualityGateVisitor(new QualityPolicy(8, 1.0));
        Fixtures.cleanProject().accept(visitor);
        assertFalse(visitor.report().complexityPassed());
        assertThrows(IllegalArgumentException.class, () -> new QualityPolicy(Double.NaN, 0.4));
        assertThrows(IllegalArgumentException.class, () -> new QualityPolicy(10, -0.1));
    }
}
