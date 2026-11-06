package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.SecurityAuditVisitor;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SecurityAuditVisitorTest {
    @Test
    void cleanProjectHasFullScoreAndNoIssues() {
        var visitor = new SecurityAuditVisitor();
        Fixtures.cleanProject().accept(visitor);
        assertEquals(100, visitor.report().score());
        assertTrue(visitor.report().issues().isEmpty());
    }

    @Test
    void reportsHardcodedCredentialsAndHighRiskSourceSeparately() {
        var visitor = new SecurityAuditVisitor();
        new SourceCodeFile("Payment.java", "payment", true, 20, 12, true, true).accept(visitor);
        assertEquals(List.of("HARDCODED_CREDENTIALS", "HIGH_RISK_SOURCE"),
                visitor.report().issues().stream().map(issue -> issue.rule()).toList());
        assertEquals(1, visitor.report().criticalIssues());
        assertEquals(60, visitor.report().score());
    }

    @Test
    void reportsEachInsecureConfigurationFlag() {
        var visitor = new SecurityAuditVisitor();
        new ConfigurationFile("app", 5, true, false, true).accept(visitor);
        assertEquals(3, visitor.report().issues().size());
        assertEquals(55, visitor.report().score());
    }

    @Test
    void vulnerableDependencyIncludesItsCoordinate() {
        var visitor = new SecurityAuditVisitor();
        new DependencyFile("pom.xml", 5, List.of(
                new Dependency("demo:unsafe", "0.1", true),
                new Dependency("demo:safe", "1.0", false))).accept(visitor);
        assertEquals(1, visitor.report().criticalIssues());
        assertTrue(visitor.report().issues().get(0).message().contains("demo:unsafe:0.1"));
    }

    @Test
    void scoreCannotBecomeNegativeAndReportsAreSnapshots() {
        var visitor = new SecurityAuditVisitor();
        var snapshot = visitor.report();
        for (int i = 0; i < 6; i++) {
            new ConfigurationFile("app" + i, 1, true, true, false).accept(visitor);
        }
        assertEquals(0, visitor.report().score());
        assertEquals(100, snapshot.score());
        assertThrows(UnsupportedOperationException.class, () -> visitor.report().issues().clear());
    }
}
