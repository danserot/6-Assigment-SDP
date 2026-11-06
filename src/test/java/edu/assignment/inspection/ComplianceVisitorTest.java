package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.ComplianceVisitor;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ComplianceVisitorTest {
    @Test
    void cleanProjectMeetsEveryRule() {
        var visitor = new ComplianceVisitor();
        Fixtures.cleanProject().accept(visitor);
        assertTrue(visitor.report().passed());
    }

    @Test
    void reportsMissingVersionAndProhibitedDependency() {
        var visitor = new ComplianceVisitor(Set.of("bad:library"));
        new DependencyFile("pom.xml", 1, List.of(new Dependency("bad:library", " ", false))).accept(visitor);
        assertEquals(List.of("MISSING_VERSION", "PROHIBITED_DEPENDENCY"),
                visitor.report().violations().stream().map(issue -> issue.rule()).toList());
    }

    @Test
    void bothSourceAndConfigurationCredentialsBreakCompliance() {
        var visitor = new ComplianceVisitor();
        new Directory("root", List.of(new SourceCodeFile("A.java", "a", false, 1, 1, true, false),
                new ConfigurationFile("app", 1, true, true, false))).accept(visitor);
        assertEquals(2, visitor.report().violations().size());
        assertFalse(visitor.report().passed());
    }

    @Test
    void testsInAnotherModuleCannotCoverAProductionModule() {
        var visitor = new ComplianceVisitor();
        new Directory("root", List.of(Fixtures.source("payment", 3), Fixtures.tests("user"))).accept(visitor);
        assertEquals("payment", visitor.report().violations().get(0).element());
    }

    @Test
    void emptyTestFileDoesNotSatisfyModuleRule() {
        var visitor = new ComplianceVisitor();
        new Directory("root", List.of(Fixtures.source("payment", 3),
                new TestFile("EmptyTest.java", "payment", 0, 0))).accept(visitor);
        assertFalse(visitor.report().passed());
    }

    @Test
    void testsBeforeSourcesWorkAndRepeatedReportsDoNotDuplicateViolations() {
        var visitor = new ComplianceVisitor();
        new Directory("root", List.of(Fixtures.tests("a"), Fixtures.source("a", 3),
                Fixtures.source("missing", 3), Fixtures.source("missing", 4))).accept(visitor);
        assertEquals(1, visitor.report().violations().size());
        assertEquals(visitor.report(), visitor.report());
    }

    @Test
    void nonProductionSourceDoesNotRequireTests() {
        var visitor = new ComplianceVisitor();
        new SourceCodeFile("Script.java", "tools", false, 5, 2, false, false).accept(visitor);
        assertTrue(visitor.report().passed());
    }
}
