package edu.assignment.inspection.model;

            import edu.assignment.inspection.visitor.ProjectVisitor;
            import java.util.Objects;


            public record TestFile(String name, String module, int linesOfCode, int testCount) implements ProjectElement {
                public TestFile {
                    name = Validation.text(name, "name");
module = Validation.text(module, "module");
Validation.nonNegative(linesOfCode, "linesOfCode");
Validation.nonNegative(testCount, "testCount");
                }

                @Override
                public void accept(ProjectVisitor visitor) {
                    Objects.requireNonNull(visitor, "visitor").visit(this);
                }
            }
