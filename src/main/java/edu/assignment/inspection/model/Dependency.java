package edu.assignment.inspection.model;

import java.util.Objects;

public record Dependency(String coordinate, String version, boolean vulnerable) {
    public Dependency {
        coordinate = Validation.text(coordinate, "coordinate");
        version = Objects.requireNonNull(version, "version").strip();
    }
}
