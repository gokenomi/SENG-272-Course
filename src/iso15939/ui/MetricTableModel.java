package iso15939.ui;

import iso15939.model.Dimension;
import iso15939.model.Metric;
import iso15939.service.MeasurementService;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MetricTableModel extends AbstractTableModel {
    private final String[] planColumns = {"Dimension", "Metric", "Coeff", "Direction", "Range", "Unit"};
    private final String[] collectColumns = {"Metric", "Direction", "Range", "Value", "Score (1-5)", "Coeff / Unit"};
    private final List<Row> rows = new ArrayList<>();
    private final boolean collectMode;
    private final MeasurementService service;

    public MetricTableModel(boolean collectMode, MeasurementService service) {
        this.collectMode = collectMode;
        this.service = service;
    }

    public void setDimensions(List<Dimension> dimensions) {
        rows.clear();
        for (Dimension dimension : dimensions) {
            for (int i = 0; i < dimension.getMetrics().size(); i++) {
                rows.add(new Row(dimension, dimension.getMetrics().get(i), i == 0));
            }
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return collectMode ? collectColumns.length : planColumns.length;
    }

    @Override
    public String getColumnName(int column) {
        return collectMode ? collectColumns[column] : planColumns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = rows.get(rowIndex);
        Metric metric = row.metric;
        if (collectMode) {
            return switch (columnIndex) {
                case 0 -> metric.getName();
                case 1 -> metric.getDirection().getDisplayName();
                case 2 -> metric.getRangeLabel();
                case 3 -> format(metric.getValue());
                case 4 -> format(service.calculateMetricScore(metric));
                case 5 -> format(metric.getCoefficient()) + " / " + metric.getUnit();
                default -> "";
            };
        }
        return switch (columnIndex) {
            case 0 -> row.dimension.getDisplayLabel();
            case 1 -> metric.getName();
            case 2 -> format(metric.getCoefficient());
            case 3 -> metric.getDirection().getDisplayName();
            case 4 -> metric.getRangeLabel();
            case 5 -> metric.getUnit();
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    private String format(double number) {
        if (number == Math.rint(number)) {
            return String.valueOf((int) number);
        }
        return String.format("%.1f", number);
    }

    private static class Row {
        private final Dimension dimension;
        private final Metric metric;
        private final boolean showDimension;

        private Row(Dimension dimension, Metric metric, boolean showDimension) {
            this.dimension = dimension;
            this.metric = metric;
            this.showDimension = showDimension;
        }
    }
}
