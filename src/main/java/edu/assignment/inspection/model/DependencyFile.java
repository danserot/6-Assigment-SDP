package edu.assignment.inspection.model;

import edu.assignment.inspection.visitor.ProjectVisitor;
import java.util.List;
import java.util.Objects;

public record DependencyFile(String name, int linesOfCode, List<Dependency> dependencies) implements ProjectElement {
    public DependencyFile {
        name = Validation.text(name, "name");
        Validation.nonNegative(linesOfCode, "linesOfCode");
        dependencies = List.copyOf(dependencies);
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        Objects.requireNonNull(visitor, "visitor").visit(this);
    }
}
