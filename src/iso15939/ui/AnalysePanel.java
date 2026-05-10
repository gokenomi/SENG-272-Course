package iso15939.ui;

import iso15939.model.DimensionResult;
import iso15939.service.MeasurementService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class AnalysePanel extends WizardPanel {
    private final AppState state;
    private final MeasurementService service;
    private final JPanel scorePanel = new JPanel();
    private final JPanel gapPanel = new JPanel();
    private final RadarChartPanel radarChartPanel = new RadarChartPanel();
    private final JLabel scenarioLabel = new JLabel(" ");

    public AnalysePanel(AppState state, MeasurementService service) {
        this.state = state;
        this.service = service;
        setLayout(new BorderLayout(18, 18));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Analyse Results");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        scenarioLabel.setForeground(new Color(82, 93, 107));
        header.add(title, BorderLayout.NORTH);
        header.add(scenarioLabel, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setOpaque(false);

        JPanel left = new JPanel(new BorderLayout(0, 14));
        left.setOpaque(false);
        left.add(scorePanel, BorderLayout.CENTER);
        left.add(gapPanel, BorderLayout.SOUTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 18, 0));
        content.setOpaque(false);
        content.add(left);
        content.add(radarChartPanel);
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void beforeShow() {
        if (state.getScenario() == null) {
            return;
        }

        scenarioLabel.setText(state.getScenario().getName() + " - weighted dimension scores and gap analysis");
        List<DimensionResult> results = service.calculateDimensionResults(state.getScenario());
        renderScores(results);
        renderGap(results);
        radarChartPanel.setResults(results);
    }

    private void renderScores(List<DimensionResult> results) {
        scorePanel.removeAll();
        JLabel heading = new JLabel("Dimension-Based Weighted Average");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 18f));
        scorePanel.add(heading);
        scorePanel.add(Box.createRigidArea(new Dimension(0, 12)));

        for (DimensionResult result : results) {
            JPanel row = new JPanel(new BorderLayout(12, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
            JLabel label = new JLabel(result.getDimension().getName());
            label.setPreferredSize(new Dimension(180, 24));
            JProgressBar bar = new JProgressBar(0, 50);
            bar.setValue((int) Math.round(result.getScore() * 10));
            bar.setStringPainted(false);
            bar.setPreferredSize(new Dimension(260, 12));
            bar.setForeground(colorForScore(result.getScore()));
            JLabel value = new JLabel(String.format("%.1f / 5.0", result.getScore()));
            value.setPreferredSize(new Dimension(70, 24));
            value.setFont(value.getFont().deriveFont(Font.BOLD, 12f));
            row.add(label, BorderLayout.WEST);
            row.add(bar, BorderLayout.CENTER);
            row.add(value, BorderLayout.EAST);
            scorePanel.add(row);
            scorePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        scorePanel.revalidate();
        scorePanel.repaint();
    }

    private void renderGap(List<DimensionResult> results) {
        gapPanel.removeAll();
        gapPanel.setLayout(new BoxLayout(gapPanel, BoxLayout.Y_AXIS));
        gapPanel.setBackground(new Color(246, 248, 251));
        gapPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(221, 226, 233)),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));

        DimensionResult lowest = service.findLowestResult(results);
        if (lowest == null) {
            return;
        }
        double gap = 5.0 - lowest.getScore();
        JLabel heading = new JLabel("Gap Analysis");
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 18f));
        gapPanel.add(heading);
        gapPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        gapPanel.add(new JLabel("Lowest dimension: " + lowest.getDimension().getName()));
        gapPanel.add(new JLabel(String.format("Score: %.1f", lowest.getScore())));
        gapPanel.add(new JLabel(String.format("Gap value: %.1f", gap)));
        gapPanel.add(new JLabel("Quality level: " + service.getQualityLabel(lowest.getScore())));
        gapPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        gapPanel.add(new JLabel("This dimension has the lowest score and requires the most improvement."));
        gapPanel.revalidate();
        gapPanel.repaint();
    }

    private Color colorForScore(double score) {
        if (score >= 4.5) {
            return new Color(32, 142, 88);
        }
        if (score >= 3.5) {
            return new Color(48, 121, 186);
        }
        if (score >= 2.5) {
            return new Color(207, 137, 35);
        }
        return new Color(190, 72, 72);
    }
}
