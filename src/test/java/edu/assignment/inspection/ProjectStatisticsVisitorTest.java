package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.ProjectStatisticsVisitor;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProjectStatisticsVisitorTest {
    @Test
    void countsAllFileTypesWithoutCountingDirectoriesAsFiles() {
        var visitor = new ProjectStatisticsVisitor();
        Fixtures.cleanProject().accept(visitor);
        var report = visitor.report();
        assertEquals(6, report.totalFiles());
        assertEquals(350, report.linesOfCode());
        assertEquals(2, report.sourceFiles());
        assertEquals(2, report.testFiles());
        assertEquals(6, report.numberOfTests());
        assertEquals(1, report.dependencies());
        assertEquals(9.0, report.averageComplexity(), 0.0001);
        assertEquals(1.0, report.testSourceRatio(), 0.0001);
    }

    @Test
    void emptyDirectoryHasFiniteZeroAverages() {
        var visitor = new ProjectStatisticsVisitor();
        new Directory("empty", List.of()).accept(visitor);
        assertEquals(0, visitor.report().totalFiles());
        assertEquals(0.0, visitor.report().averageComplexity());
        assertEquals(0.0, visitor.report().testSourceRatio());
    }

    @Test
    void countsTestMethodsSeparatelyFromTestFileRatio() {
        var visitor = new ProjectStatisticsVisitor();
        new Directory("root", List.of(Fixtures.source("a", 2), Fixtures.source("b", 3),
                new TestFile("ATest.java", "a", 10, 20))).accept(visitor);
        assertEquals(20, visitor.report().numberOfTests());
        assertEquals(0.5, visitor.report().testSourceRatio());
        assertEquals(2.5, visitor.report().averageComplexity());
    }

    @Test
    void testOnlyProjectDoesNotDivideByZero() {
        var visitor = new ProjectStatisticsVisitor();
        Fixtures.tests("orphan").accept(visitor);
        assertEquals(1, visitor.report().totalFiles());
        assertEquals(0.0, visitor.report().testSourceRatio());
    }

    @Test
    void totalsDoNotOverflowAtIntegerMaxValue() {
        var visitor = new ProjectStatisticsVisitor();
        var file = new SourceCodeFile("Big.java", "big", true, Integer.MAX_VALUE,
                Integer.MAX_VALUE, false, false);
        new Directory("root", List.of(file, file)).accept(visitor);
        assertEquals(4294967294L, visitor.report().linesOfCode());
        assertEquals((double) Integer.MAX_VALUE, visitor.report().averageComplexity());
    }
}
