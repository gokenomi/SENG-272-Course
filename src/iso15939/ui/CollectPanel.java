package iso15939.ui;

import iso15939.service.MeasurementService;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

public class CollectPanel extends WizardPanel {
    private final AppState state;
    private final MetricTableModel tableModel;
    private final JLabel scenarioLabel = new JLabel(" ");

    public CollectPanel(AppState state, MeasurementService service) {
        this.state = state;
        this.tableModel = new MetricTableModel(true, service);
        setLayout(new BorderLayout(0, 16));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Collect Data");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        scenarioLabel.setForeground(new Color(82, 93, 107));
        header.add(title, BorderLayout.NORTH);
        header.add(scenarioLabel, BorderLayout.SOUTH);

        JTable table = new JTable(tableModel);
        table.setRowHeight(38);
        table.getTableHeader().setReorderingAllowed(false);
        table.setFillsViewportHeight(true);
        styleTable(table);

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    @Override
    public void beforeShow() {
        if (state.getScenario() != null) {
            scenarioLabel.setText("Raw metric values and calculated scores for " + state.getScenario().getName());
            tableModel.setDimensions(state.getScenario().getDimensions());
        }
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
        table.setDefaultRenderer(Object.class, new CollectCellRenderer());
        table.getColumnModel().getColumn(0).setPreferredWidth(170);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(150);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
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

    private static class CollectCellRenderer extends DefaultTableCellRenderer {
        private final Color scoreBackground = new Color(214, 244, 225);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            setFont(table.getFont().deriveFont(column == 4 ? Font.BOLD : Font.PLAIN, 15f));
            setHorizontalAlignment(LEFT);
            if (!isSelected) {
                setBackground(column == 4 ? scoreBackground : Color.WHITE);
                setForeground(Color.BLACK);
            }
            return component;
        }
    }
}
