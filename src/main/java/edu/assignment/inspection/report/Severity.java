package edu.assignment.inspection.report;

public enum Severity {
    CRITICAL(25), HIGH(15), MEDIUM(5);

    private final int penalty;

    Severity(int penalty) { this.penalty = penalty; }
    public int penalty() { return penalty; }
}
