package edu.assignment.inspection.model;

import edu.assignment.inspection.visitor.ProjectVisitor;
import java.util.Objects;

public record ConfigurationFile(String name, int linesOfCode, boolean exposedCredentials,
        boolean secureTransportEnabled, boolean debugEnabled) implements ProjectElement {
    public ConfigurationFile {
        name = Validation.text(name, "name");
        Validation.nonNegative(linesOfCode, "linesOfCode");
    }

    @Override
    public void accept(ProjectVisitor visitor) {
        Objects.requireNonNull(visitor, "visitor").visit(this);
    }
}
