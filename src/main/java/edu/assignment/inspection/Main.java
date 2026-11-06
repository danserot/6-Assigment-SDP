package edu.assignment.inspection;

import edu.assignment.inspection.demo.ConsoleReporter;
import edu.assignment.inspection.demo.DemoProjects;
import edu.assignment.inspection.model.Directory;
import edu.assignment.inspection.visitor.*;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        System.out.println("Smart Code Inspection and Compliance Engine");
        System.out.println("Author: Artem Khloptsev | Assignment 6 | Visitor pattern");
        System.out.println("Demo uses supplied file metadata; dependency flags are educational fixtures.");
        inspect(DemoProjects.fintechProject());
        inspect(DemoProjects.releaseReadyProject());
    }

    private static void inspect(Directory project) {
        System.out.println("\n=== " + project.name() + " ===");
        var reporter = new ConsoleReporter(System.out);
        var security = new SecurityAuditVisitor();
        var statistics = new ProjectStatisticsVisitor();
        var quality = new QualityGateVisitor();
        var compliance = new ComplianceVisitor();

        project.accept(security);
        project.accept(statistics);
        project.accept(quality);
        project.accept(compliance);

        reporter.security(security.report());
        reporter.statistics(statistics.report());
        reporter.quality(quality.report());
        reporter.compliance(compliance.report());
    }
}
