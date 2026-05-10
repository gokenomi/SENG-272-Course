package iso15939.ui;

import iso15939.data.ScenarioRepository;
import iso15939.model.Mode;
import iso15939.model.QualityType;
import iso15939.model.Scenario;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class DefinePanel extends WizardPanel {
    private final AppState state;
    private final ScenarioRepository repository;
    private final JComboBox<Scenario> scenarioBox = new JComboBox<>();
    private final JLabel scenarioLabel = new JLabel("Scenario");
    private final JLabel scenarioDescription = new JLabel(" ");
    private final JRadioButton productButton = new JRadioButton("Product Quality");
    private final JRadioButton processButton = new JRadioButton("Process Quality");
    private final JRadioButton customButton = new JRadioButton("Custom");
    private final JRadioButton educationButton = new JRadioButton("Education");
    private final JRadioButton healthButton = new JRadioButton("Health");

    public DefinePanel(AppState state, ScenarioRepository repository) {
        this.state = state;
        this.repository = repository;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel("Define Quality Dimensions");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 16);
        gbc.anchor = GridBagConstraints.WEST;

        ButtonGroup qualityGroup = new ButtonGroup();
        qualityGroup.add(productButton);
        qualityGroup.add(processButton);
        productButton.setSelected(true);

        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(customButton);
        modeGroup.add(educationButton);
        modeGroup.add(healthButton);
        educationButton.setSelected(true);

        addSection(form, gbc, 0, "Quality Type", productButton, processButton);
        addSection(form, gbc, 1, "Mode", customButton, healthButton, educationButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(scenarioLabel, gbc);

        gbc.gridx = 1;
        scenarioBox.setPrototypeDisplayValue(new Scenario("Scenario C - Team Alpha", QualityType.PRODUCT, Mode.EDUCATION,
                "", java.util.List.of()));
        form.add(scenarioBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        scenarioDescription.setForeground(new Color(82, 93, 107));
        form.add(scenarioDescription, gbc);

        productButton.addActionListener(event -> refreshModeView());
        processButton.addActionListener(event -> refreshModeView());
        customButton.addActionListener(event -> refreshModeView());
        educationButton.addActionListener(event -> refreshModeView());
        healthButton.addActionListener(event -> refreshModeView());
        scenarioBox.addActionListener(event -> updateDescription());

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(form, BorderLayout.NORTH);
        add(formWrapper, BorderLayout.CENTER);
        refreshModeView();
    }

    @Override
    public boolean beforeNext() {
        state.setQualityType(getSelectedQualityType());
        state.setMode(getSelectedMode());

        if (customButton.isSelected()) {
            state.setScenario(null);
            return true;
        }

        Scenario selected = (Scenario) scenarioBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a scenario to continue.",
                    "Missing Scenario", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        state.setScenario(selected);
        return true;
    }

    private void refreshModeView() {
        boolean custom = customButton.isSelected();
        scenarioLabel.setVisible(!custom);
        scenarioBox.setVisible(!custom);
        if (custom) {
            scenarioDescription.setText("Custom mode selected. Define your own dimensions and metrics in the Plan step.");
        } else {
            refreshScenarios();
        }
        revalidate();
        repaint();
    }

    private void refreshScenarios() {
        Mode mode = getSelectedMode();
        state.setQualityType(getSelectedQualityType());
        state.setMode(mode);

        DefaultComboBoxModel<Scenario> model = new DefaultComboBoxModel<>();
        for (Scenario scenario : repository.findByModeAndQualityType(mode, getSelectedQualityType())) {
            model.addElement(scenario);
        }
        scenarioBox.setModel(model);
        if (model.getSize() > 0) {
            scenarioBox.setSelectedIndex(0);
        }
        updateDescription();
    }

    private void updateDescription() {
        Scenario selected = (Scenario) scenarioBox.getSelectedItem();
        if (selected == null) {
            scenarioDescription.setText("No scenario is available for this mode.");
            return;
        }
        scenarioDescription.setText(selected.getDescription() + " Scenario type: " + selected.getQualityType().getDisplayName() + ".");
    }

    private QualityType getSelectedQualityType() {
        return productButton.isSelected() ? QualityType.PRODUCT : QualityType.PROCESS;
    }

    private Mode getSelectedMode() {
        if (customButton.isSelected()) {
            return Mode.CUSTOM;
        }
        return educationButton.isSelected() ? Mode.EDUCATION : Mode.HEALTH;
    }

    private void addSection(JPanel form, GridBagConstraints gbc, int row, String title, JRadioButton... buttons) {
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(boldLabel(title), gbc);

        JPanel options = new JPanel();
        options.setOpaque(false);
        for (JRadioButton button : buttons) {
            options.add(button);
        }
        gbc.gridx = 1;
        form.add(options, gbc);
    }

    private JLabel boldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }
}
