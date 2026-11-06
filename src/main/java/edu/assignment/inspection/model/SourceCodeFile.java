package edu.assignment.inspection.model;

            import edu.assignment.inspection.visitor.ProjectVisitor;
            import java.util.Objects;


            public record SourceCodeFile(String name, String module, boolean production, int linesOfCode,
                                     int complexity, boolean hardcodedCredentials, boolean highRisk) implements ProjectElement {
                public SourceCodeFile {
                    name = Validation.text(name, "name");
module = Validation.text(module, "module");
Validation.nonNegative(linesOfCode, "linesOfCode");
Validation.nonNegative(complexity, "complexity");
                }

                @Override
                public void accept(ProjectVisitor visitor) {
                    Objects.requireNonNull(visitor, "visitor").visit(this);
                }
            }
