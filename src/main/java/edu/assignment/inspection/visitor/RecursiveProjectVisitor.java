package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.ConfigurationFile;
import edu.assignment.inspection.model.DependencyFile;
import edu.assignment.inspection.model.Directory;
import edu.assignment.inspection.model.SourceCodeFile;
import edu.assignment.inspection.model.TestFile;

/** Owns traversal: each directory dispatches each child exactly once. */
public abstract class RecursiveProjectVisitor implements ProjectVisitor {
    @Override
    public void visit(Directory directory) {
        directory.children().forEach(child -> child.accept(this));
    }

    @Override
    public void visit(SourceCodeFile file) { }

    @Override
    public void visit(TestFile file) { }

    @Override
    public void visit(ConfigurationFile file) { }

    @Override
    public void visit(DependencyFile file) { }
}
