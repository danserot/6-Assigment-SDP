package edu.assignment.inspection;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.visitor.RecursiveProjectVisitor;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ElementTest {
    @Test
    void dispatchesEveryElementToItsOwnOverload() {
        List<String> visited = new ArrayList<>();
        var visitor = new RecursiveProjectVisitor() {
            public void visit(SourceCodeFile file) { visited.add("source"); }
            public void visit(TestFile file) { visited.add("test"); }
            public void visit(ConfigurationFile file) { visited.add("config"); }
            public void visit(DependencyFile file) { visited.add("dependencies"); }
            public void visit(Directory directory) {
                visited.add("directory");
                super.visit(directory);
            }
        };
        ProjectElement tree = new Directory("root", List.of(Fixtures.source("payment", 5),
                Fixtures.tests("payment"), new ConfigurationFile("app", 3, false, true, false),
                new DependencyFile("pom.xml", 5, List.of())));
        tree.accept(visitor);
        assertEquals(List.of("directory", "source", "test", "config", "dependencies"), visited);
    }

    @Test
    void visitsNestedDirectoriesInOrder() {
        List<String> names = new ArrayList<>();
        var visitor = new RecursiveProjectVisitor() {
            public void visit(SourceCodeFile file) { names.add(file.module()); }
        };
        new Directory("root", List.of(new Directory("src", List.of(
                new Directory("nested", List.of(Fixtures.source("deep", 1))))),
                Fixtures.source("top", 2))).accept(visitor);
        assertEquals(List.of("deep", "top"), names);
    }

    @Test
    void emptyDirectoryDoesNotDispatchAnyLeaves() {
        int[] count = {0};
        new Directory("empty", List.of()).accept(new RecursiveProjectVisitor() {
            public void visit(SourceCodeFile file) { count[0]++; }
        });
        assertEquals(0, count[0]);
    }

    @Test
    void directoryCopiesTheProvidedChildren() {
        List<ProjectElement> children = new ArrayList<>();
        children.add(Fixtures.source("first", 3));
        var directory = new Directory("root", children);
        children.clear();
        assertEquals(1, directory.children().size());
        assertThrows(UnsupportedOperationException.class, () -> directory.children().clear());
    }

    @Test
    void rejectsNegativeMetricsAndBlankNames() {
        assertThrows(IllegalArgumentException.class, () -> Fixtures.source("payment", -1));
        assertThrows(IllegalArgumentException.class, () -> new TestFile("x", "m", 2, -1));
        assertThrows(IllegalArgumentException.class, () -> new Directory(" ", List.of()));
    }

    @Test
    void rejectsNullChildrenAndNullVisitor() {
        assertThrows(NullPointerException.class, () -> new Directory("x", java.util.Arrays.asList((ProjectElement) null)));
        assertThrows(NullPointerException.class, () -> Fixtures.source("payment", 3).accept(null));
    }
}
