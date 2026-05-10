package iso15939.ui;

import iso15939.data.ScenarioRepository;
import iso15939.service.MeasurementService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final AppState state = new AppState();
    private final ScenarioRepository repository = new ScenarioRepository();
    private final MeasurementService service = new MeasurementService();
    private final StepIndicator stepIndicator = new StepIndicator();
    private final JPanel cardPanel = new JPanel(new CardLayout());
    private final List<WizardPanel> panels = new ArrayList<>();
    private final JButton backButton = new JButton("Back");
    private final JButton nextButton = new JButton("Next");
    private int currentStep = 0;

    public MainFrame() {
        super("ISO 15939 Measurement Process Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 760);
        setMinimumSize(getSize());
        setLocationRelativeTo(null);

        panels.add(new ProfilePanel(state));
        panels.add(new DefinePanel(state, repository));
        panels.add(new PlanPanel(state));
        panels.add(new CollectPanel(state, service));
        panels.add(new AnalysePanel(state, service));

        for (int i = 0; i < panels.size(); i++) {
            cardPanel.add(panels.get(i), String.valueOf(i));
        }

        add(stepIndicator, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        add(createNavigation(), BorderLayout.SOUTH);

        showStep(0);
    }

    private JPanel createNavigation() {
        JPanel navigation = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        navigation.setBackground(new Color(245, 247, 250));
        navigation.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 227, 233)));

        backButton.addActionListener(event -> showStep(currentStep - 1));
        nextButton.addActionListener(event -> {
            if (panels.get(currentStep).beforeNext()) {
                if (currentStep == panels.size() - 1) {
                    showStep(0);
                } else {
                    showStep(currentStep + 1);
                }
            }
        });

        navigation.add(backButton);
        navigation.add(nextButton);
        return navigation;
    }

    private void showStep(int step) {
        if (step < 0 || step >= panels.size()) {
            return;
        }
        currentStep = step;
        panels.get(currentStep).beforeShow();
        ((CardLayout) cardPanel.getLayout()).show(cardPanel, String.valueOf(currentStep));
        stepIndicator.updateStep(currentStep);
        backButton.setEnabled(currentStep > 0);
        nextButton.setText(currentStep == panels.size() - 1 ? "Start New Session" : "Next");
    }
}
