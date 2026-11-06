package edu.assignment.inspection;

import edu.assignment.inspection.demo.DemoProjects;
import edu.assignment.inspection.visitor.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class DemoIntegrationTest {
    @Test
    void allVisitorsInspectTheSameFintechTreeWithoutMutatingIt() {
        var project = DemoProjects.fintechProject();
        var security = new SecurityAuditVisitor();
        var statistics = new ProjectStatisticsVisitor();
        var quality = new QualityGateVisitor();
        var compliance = new ComplianceVisitor();
        project.accept(security);
        project.accept(statistics);
        project.accept(quality);
        project.accept(compliance);
        assertEquals(3, security.report().criticalIssues());
        assertEquals(5, statistics.report().totalFiles());
        assertEquals(374, statistics.report().linesOfCode());
        assertEquals(10.0, statistics.report().averageComplexity());
        assertEquals(0.5, statistics.report().testSourceRatio());
        assertEquals(statistics.report(), quality.report().statistics());
        assertFalse(quality.report().passed());
        assertEquals(5, compliance.report().violations().size());
        var repeat = new ProjectStatisticsVisitor();
        project.accept(repeat);
        assertEquals(statistics.report(), repeat.report());
    }

    @Test
    void correctedScenarioPassesSecurityQualityAndCompliance() {
        var project = DemoProjects.releaseReadyProject();
        var security = new SecurityAuditVisitor();
        var quality = new QualityGateVisitor();
        var compliance = new ComplianceVisitor();
        project.accept(security);
        project.accept(quality);
        project.accept(compliance);
        assertEquals(100, security.report().score());
        assertTrue(quality.report().passed());
        assertTrue(compliance.report().passed());
    }

    @Test
    void consoleDemoShowsBothReleaseOutcomesAndReports() {
        var original = System.out;
        var buffer = new ByteArrayOutputStream();
        try (var output = new PrintStream(buffer, true, StandardCharsets.UTF_8)) {
            System.setOut(output);
            Main.main(new String[0]);
        } finally {
            System.setOut(original);
        }
        String output = buffer.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("FINAL RESULT: FAILED"));
        assertTrue(output.contains("FINAL RESULT: PASSED"));
        assertTrue(output.contains("PROJECT STATISTICS"));
        assertTrue(output.contains("COMPLIANCE: PASS"));
        assertTrue(output.contains("VULNERABLE_DEPENDENCY"));
    }
}
