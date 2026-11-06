package edu.assignment.inspection.visitor;

import edu.assignment.inspection.model.*;
import edu.assignment.inspection.report.ProjectStatistics;

public final class ProjectStatisticsVisitor extends RecursiveProjectVisitor {
    private long totalFiles;
    private long sourceFiles;
    private long testFiles;
    private long linesOfCode;
    private long numberOfTests;
    private long dependencies;
    private long totalComplexity;

    @Override
    public void visit(SourceCodeFile file) {
        countFile(file.linesOfCode());
        sourceFiles++;
        totalComplexity += file.complexity();
    }

    @Override
    public void visit(TestFile file) {
        countFile(file.linesOfCode());
        testFiles++;
        numberOfTests += file.testCount();
    }

    @Override
    public void visit(ConfigurationFile file) { countFile(file.linesOfCode()); }

    @Override
    public void visit(DependencyFile file) {
        countFile(file.linesOfCode());
        dependencies += file.dependencies().size();
    }

    public ProjectStatistics report() {
        return new ProjectStatistics(totalFiles, sourceFiles, testFiles, linesOfCode,
                numberOfTests, dependencies, totalComplexity);
    }

    private void countFile(int lines) {
        totalFiles++;
        linesOfCode += lines;
    }
}
