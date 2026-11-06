package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.ConfigurationFile;
import edu.assignment.inspection.model.DependencyFile;
import edu.assignment.inspection.model.Directory;
import edu.assignment.inspection.model.SourceCodeFile;
import edu.assignment.inspection.model.TestFile;

public interface ProjectVisitor {
    void visit(SourceCodeFile file);
    void visit(TestFile file);
    void visit(ConfigurationFile file);
    void visit(DependencyFile file);
    void visit(Directory directory);
}
