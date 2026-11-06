package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import java.util.List;

final class Fixtures {
    private Fixtures() { }

    static SourceCodeFile source(String module, int complexity) {
        return new SourceCodeFile(module + "Service.java", module, true, 100,
                complexity, false, false);
    }

    static TestFile tests(String module) {
        return new TestFile(module + "ServiceTest.java", module, 50, 3);
    }

    static Directory cleanProject() {
        return new Directory("clean", List.of(
                new Directory("src", List.of(source("payment", 8), source("user", 10))),
                new Directory("tests", List.of(tests("payment"), tests("user"))),
                new ConfigurationFile("application.properties", 20, false, true, false),
                new DependencyFile("pom.xml", 30,
                        List.of(new Dependency("org.example:safe", "1.0.0", false)))));
    }
}
