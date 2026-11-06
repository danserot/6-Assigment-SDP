package edu.assignment.inspection.report;

import java.util.List;

public record TechnicalDebtReport(List<TechnicalDebtItem> items) {
    public TechnicalDebtReport { items = List.copyOf(items); }
    public long totalMinutes() { return items.stream().mapToLong(TechnicalDebtItem::estimatedMinutes).sum(); }
}
