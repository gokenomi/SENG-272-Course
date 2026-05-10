package iso15939.ui;

import iso15939.model.Dimension;
import iso15939.model.Direction;
import iso15939.model.Metric;
import iso15939.model.Mode;
import iso15939.model.Scenario;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlanPanel extends WizardPanel {
    private final AppState state;
    private final MetricTableModel tableModel = new MetricTableModel(false, null);
    private final JLabel scenarioLabel = new JLabel(" ");
    private final JPanel contentPanel = new JPanel(new CardLayout());
    private final JPanel predefinedPanel = new JPanel(new BorderLayout());
    private final JPanel customPanel = new JPanel(new BorderLayout(0, 10));
    private final JTextField customScenarioName = new JTextField("Custom Scenario", 24);
    private final DefaultTableModel customTableModel = new DefaultTableModel(
            new String[]{"Dimension", "Dim Coeff", "Metric", "Metric Coeff", "Direction", "Min", "Max", "Unit", "Value"},
            0
    );

    public PlanPanel(AppState state) {
        this.state = state;
        setLayout(new BorderLayout(0, 16));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Plan Measurement");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        scenarioLabel.setForeground(new Color(82, 93, 107));
        header.add(title, BorderLayout.NORTH);
        header.add(scenarioLabel, BorderLayout.SOUTH);

        JTable table = new JTable(tableModel);
        table.setRowHeight(38);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        styleTable(table);
        predefinedPanel.setOpaque(false);
        predefinedPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        buildCustomPanel();
        contentPanel.setOpaque(false);
        contentPanel.add(predefinedPanel, "predefined");
        contentPanel.add(customPanel, "custom");

        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        addDefaultCustomRows();
    }

    @Override
    public void beforeShow() {
        if (state.getMode() == Mode.CUSTOM) {
            scenarioLabel.setText("Custom mode - define your own dimensions and metrics from scratch.");
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "custom");
            return;
        }

        if (state.getScenario() != null) {
            scenarioLabel.setText(state.getScenario().getName() + " - " + state.getScenario().getDescription());
            tableModel.setDimensions(state.getScenario().getDimensions());
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "predefined");
        }
    }

    @Override
    public boolean beforeNext() {
        if (state.getMode() != Mode.CUSTOM) {
            return true;
        }

        Scenario customScenario = buildCustomScenario();
        if (customScenario == null) {
            return false;
        }
        state.setScenario(customScenario);
        return true;
    }

    private void buildCustomPanel() {
        customPanel.setOpaque(false);
        customPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 226, 233)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        top.add(boldLabel("Custom Scenario Name"), gbc);
        gbc.gridx = 1;
        top.add(customScenarioName, gbc);

        JTable customTable = new JTable(customTableModel);
        customTable.setRowHeight(28);
        customTable.getTableHeader().setReorderingAllowed(false);
        customTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(customTable);
        scrollPane.setPreferredSize(new java.awt.Dimension(900, 170));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton addButton = new JButton("Add Metric Row");
        JButton removeButton = new JButton("Remove Selected Row");
        addButton.addActionListener(event -> addCustomRow("", 0, "", 50, "Higher", 0, 100, "%", 0));
        removeButton.addActionListener(event -> {
            int selectedRow = customTable.getSelectedRow();
            if (selectedRow >= 0) {
                customTableModel.removeRow(selectedRow);
            }
        });
        buttons.add(addButton);
        buttons.add(removeButton);

        customPanel.add(top, BorderLayout.NORTH);
        customPanel.add(scrollPane, BorderLayout.CENTER);
        customPanel.add(buttons, BorderLayout.SOUTH);
    }

    private Scenario buildCustomScenario() {
        String scenarioName = customScenarioName.getText().trim();
        if (scenarioName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a custom scenario name.",
                    "Missing Custom Scenario", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        if (customTableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Please add at least one custom metric row.",
                    "Missing Custom Metrics", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Map<String, CustomDimensionBuilder> builders = new LinkedHashMap<>();
        for (int row = 0; row < customTableModel.getRowCount(); row++) {
            String dimensionName = getCellText(row, 0);
            String metricName = getCellText(row, 2);
            String unit = getCellText(row, 7);
            String directionText = getCellText(row, 4);
            if (dimensionName.isEmpty() || metricName.isEmpty() || unit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill Dimension, Metric, and Unit for every custom row.",
                        "Incomplete Custom Row", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            double dimensionCoefficient = parseNumber(row, 1, "dimension coefficient");
            double metricCoefficient = parseNumber(row, 3, "metric coefficient");
            double minimum = parseNumber(row, 5, "minimum value");
            double maximum = parseNumber(row, 6, "maximum value");
            double value = parseNumber(row, 8, "metric value");
            if (Double.isNaN(dimensionCoefficient) || Double.isNaN(metricCoefficient)
                    || Double.isNaN(minimum) || Double.isNaN(maximum) || Double.isNaN(value)) {
                return null;
            }
            if (maximum <= minimum) {
                JOptionPane.showMessageDialog(this, "Maximum value must be greater than minimum value.",
                        "Invalid Range", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            Direction direction = directionText.equalsIgnoreCase("Lower") ? Direction.LOWER_BETTER : Direction.HIGHER_BETTER;
            CustomDimensionBuilder builder = builders.computeIfAbsent(dimensionName,
                    key -> new CustomDimensionBuilder(dimensionName, dimensionCoefficient));
            builder.metrics.add(new Metric(metricName, metricCoefficient, direction, minimum, maximum, unit, value));
        }

        List<Dimension> dimensions = new ArrayList<>();
        for (CustomDimensionBuilder builder : builders.values()) {
            dimensions.add(new Dimension(builder.name, builder.coefficient, builder.metrics));
        }
        return new Scenario(scenarioName, state.getQualityType(), Mode.CUSTOM,
                "User-defined custom measurement scenario.", dimensions);
    }

    private String getCellText(int row, int column) {
        Object value = customTableModel.getValueAt(row, column);
        return value == null ? "" : value.toString().trim();
    }

    private double parseNumber(int row, int column, String label) {
        try {
            return Double.parseDouble(getCellText(row, column));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for " + label + ".",
                    "Invalid Custom Value", JOptionPane.WARNING_MESSAGE);
            return Double.NaN;
        }
    }

    private void addDefaultCustomRows() {
        addCustomRow("Custom Usability", 50, "Task success rate", 50, "Higher", 0, 100, "%", 80);
        addCustomRow("Custom Usability", 50, "Task completion time", 50, "Lower", 0, 120, "sec", 35);
        addCustomRow("Custom Reliability", 50, "Availability", 50, "Higher", 95, 100, "%", 98);
        addCustomRow("Custom Reliability", 50, "Recovery time", 50, "Lower", 0, 60, "min", 12);
    }

    private void addCustomRow(String dimension, double dimensionCoefficient, String metric, double metricCoefficient,
                              String direction, double minimum, double maximum, String unit, double value) {
        customTableModel.addRow(new Object[]{
                dimension,
                formatNumber(dimensionCoefficient),
                metric,
                formatNumber(metricCoefficient),
                direction,
                formatNumber(minimum),
                formatNumber(maximum),
                unit,
                formatNumber(value)
        });
    }

    private String formatNumber(double number) {
        if (number == Math.rint(number)) {
            return String.valueOf((int) number);
        }
        return String.format("%.1f", number);
    }

    private JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }

    private void styleTable(JTable table) {
        Color headerBlue = new Color(47, 122, 185);
        table.getTableHeader().setBackground(headerBlue);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 15f));
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer(headerBlue));
        table.setGridColor(new Color(203, 207, 212));
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setDefaultRenderer(Object.class, new PlanCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(190);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(140);
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        private final Color background;

        private HeaderRenderer(Color background) {
            this.background = background;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(background);
            setForeground(Color.BLACK);
            setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 15f));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            setHorizontalAlignment(LEFT);
            return this;
        }
    }

    private static class PlanCellRenderer extends DefaultTableCellRenderer {
        private final Color dimensionBackground = new Color(232, 244, 252);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            setFont(table.getFont().deriveFont(15f));
            setHorizontalAlignment(LEFT);
            if (!isSelected) {
                setBackground(column == 0 && value != null && !value.toString().isBlank()
                        ? dimensionBackground
                        : Color.WHITE);
                setForeground(Color.BLACK);
            }
            return component;
        }
    }

    private static class CustomDimensionBuilder {
        private final String name;
        private final double coefficient;
        private final List<Metric> metrics = new ArrayList<>();

        private CustomDimensionBuilder(String name, double coefficient) {
            this.name = name;
            this.coefficient = coefficient;
        }
    }
}
