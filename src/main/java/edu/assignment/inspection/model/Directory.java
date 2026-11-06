package edu.assignment.inspection.model;

            import edu.assignment.inspection.visitor.ProjectVisitor;
            import java.util.Objects;
            import java.util.List;

            public record Directory(String name, List<ProjectElement> children) implements ProjectElement {
                public Directory {
                    name = Validation.text(name, "name");
children = List.copyOf(children);
                }

                @Override
                public void accept(ProjectVisitor visitor) {
                    Objects.requireNonNull(visitor, "visitor").visit(this);
                }
            }
