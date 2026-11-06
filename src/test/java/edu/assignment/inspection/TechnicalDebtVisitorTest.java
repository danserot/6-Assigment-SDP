package edu.assignment.inspection;

import edu.assignment.inspection.demo.DemoProjects;
import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.TechnicalDebtVisitor;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TechnicalDebtVisitorTest {
    @Test
    void fintechDebtIncludesComplexityCredentialsRiskAndMissingTests() {
        var visitor = new TechnicalDebtVisitor();
        DemoProjects.fintechProject().accept(visitor);
        assertEquals(285, visitor.report().totalMinutes());
        assertEquals(4, visitor.report().items().size());
    }

    @Test
    void releaseReadyProjectHasNoEstimatedDebt() {
        var visitor = new TechnicalDebtVisitor();
        DemoProjects.releaseReadyProject().accept(visitor);
        assertEquals(0, visitor.report().totalMinutes());
        assertTrue(visitor.report().items().isEmpty());
    }

    @Test
    void missingTestsAreChargedOncePerModuleRegardlessOfOrder() {
        var visitor = new TechnicalDebtVisitor();
        new Directory("root", List.of(Fixtures.tests("tested"), Fixtures.source("tested", 10),
                Fixtures.source("missing", 10), Fixtures.source("missing", 10))).accept(visitor);
        assertEquals(120, visitor.report().totalMinutes());
        assertEquals(visitor.report(), visitor.report());
    }

    @Test
    void extremeComplexityUsesLongArithmetic() {
        var visitor = new TechnicalDebtVisitor();
        new SourceCodeFile("Big.java", "tools", false, 1, Integer.MAX_VALUE, false, false).accept(visitor);
        assertEquals(64424509110L, visitor.report().totalMinutes());
    }

    @Test
    void emptyDirectoryAndEmptyTestsDoNotHideModuleDebt() {
        var visitor = new TechnicalDebtVisitor();
        new Directory("empty", List.of()).accept(visitor);
        assertEquals(0, visitor.report().totalMinutes());
        Fixtures.source("payment", 10).accept(visitor);
        new TestFile("EmptyTest.java", "payment", 0, 0).accept(visitor);
        assertEquals(120, visitor.report().totalMinutes());
    }
}
