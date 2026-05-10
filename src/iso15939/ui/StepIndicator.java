package iso15939.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class StepIndicator extends JPanel {
    private final List<JLabel> labels = new ArrayList<>();
    private final String[] names = {"Profile", "Define", "Plan", "Collect", "Analyse"};

    public StepIndicator() {
        setLayout(new GridLayout(1, names.length, 8, 0));
        setBorder(BorderFactory.createEmptyBorder(14, 18, 10, 18));
        setBackground(new Color(245, 247, 250));
        for (int i = 0; i < names.length; i++) {
            JLabel label = new JLabel((i + 1) + ". " + names[i], JLabel.CENTER);
            label.setOpaque(true);
            label.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            labels.add(label);
            add(label);
        }
        updateStep(0);
    }

    public void updateStep(int activeIndex) {
        for (int i = 0; i < labels.size(); i++) {
            JLabel label = labels.get(i);
            if (i < activeIndex) {
                label.setText("\u2713 " + names[i]);
                label.setBackground(new Color(226, 246, 236));
                label.setForeground(new Color(20, 108, 67));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else if (i == activeIndex) {
                label.setText((i + 1) + ". " + names[i]);
                label.setBackground(new Color(28, 92, 157));
                label.setForeground(Color.WHITE);
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setText((i + 1) + ". " + names[i]);
                label.setBackground(new Color(235, 239, 244));
                label.setForeground(new Color(69, 79, 91));
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            }
        }
    }
}
