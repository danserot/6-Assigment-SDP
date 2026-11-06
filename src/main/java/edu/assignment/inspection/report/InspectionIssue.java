package edu.assignment.inspection.report;

public record InspectionIssue(Severity severity, String rule, String element, String message) { }
