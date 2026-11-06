package edu.assignment.inspection.model;

import edu.assignment.inspection.visitor.ProjectVisitor;

public interface ProjectElement {
    void accept(ProjectVisitor visitor);
}
