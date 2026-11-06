package edu.assignment.inspection.demo;

import edu.assignment.inspection.model.*;
import java.util.List;

/** Explicit sample metadata; this demo does not parse files or query a vulnerability service. */
public final class DemoProjects {
    private DemoProjects() { }

    public static Directory fintechProject() {
        return new Directory("fintech-project", List.of(
                new Directory("src", List.of(
                        new SourceCodeFile("PaymentService.java", "payment", true, 140, 12, true, true),
                        new SourceCodeFile("UserService.java", "user", true, 100, 8, false, false))),
                new Directory("tests", List.of(
                        new TestFile("PaymentServiceTest.java", "payment", 70, 6))),
                new Directory("config", List.of(
                        new ConfigurationFile("application.properties", 24, true, false, true))),
                new DependencyFile("pom.xml", 40, List.of(
                        new Dependency("com.legacy:unsafe-crypto", "0.8", true),
                        new Dependency("org.example:metrics", "", false),
                        new Dependency("org.junit.jupiter:junit-jupiter", "5.10.2", false)))));
    }

    public static Directory releaseReadyProject() {
        return new Directory("fintech-project-fixed", List.of(
                new Directory("src", List.of(
                        new SourceCodeFile("PaymentService.java", "payment", true, 140, 7, false, false),
                        new SourceCodeFile("UserService.java", "user", true, 100, 6, false, false))),
                new Directory("tests", List.of(
                        new TestFile("PaymentServiceTest.java", "payment", 70, 6),
                        new TestFile("UserServiceTest.java", "user", 50, 4))),
                new Directory("config", List.of(
                        new ConfigurationFile("application.properties", 24, false, true, false))),
                new DependencyFile("pom.xml", 40,
                        List.of(new Dependency("org.example:metrics", "1.2.0", false)))));
    }
}
