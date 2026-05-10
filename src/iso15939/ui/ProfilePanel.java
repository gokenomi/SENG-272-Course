package iso15939.ui;

import iso15939.model.Profile;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class ProfilePanel extends WizardPanel {
    private final AppState state;
    private final JTextField usernameField = new JTextField(24);
    private final JTextField schoolField = new JTextField(24);
    private final JTextField sessionField = new JTextField(24);

    public ProfilePanel(AppState state) {
        this.state = state;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel title = new JLabel("Profile");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        JLabel subtitle = new JLabel("Enter the user and session information before starting the measurement process.");
        subtitle.setForeground(new Color(82, 93, 107));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 8)));
        header.add(subtitle);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 12);
        gbc.anchor = GridBagConstraints.WEST;

        addField(form, gbc, 0, "Username", usernameField);
        addField(form, gbc, 1, "School", schoolField);
        addField(form, gbc, 2, "Session Name", sessionField);

        add(form, BorderLayout.CENTER);
    }

    @Override
    public boolean beforeNext() {
        if (usernameField.getText().trim().isEmpty()) {
            showWarning("Please enter your username to continue.");
            return false;
        }
        if (schoolField.getText().trim().isEmpty()) {
            showWarning("Please enter your school name to continue.");
            return false;
        }
        if (sessionField.getText().trim().isEmpty()) {
            showWarning("Please enter a session name to continue.");
            return false;
        }

        Profile profile = state.getProfile();
        profile.setUsername(usernameField.getText().trim());
        profile.setSchool(schoolField.getText().trim());
        profile.setSessionName(sessionField.getText().trim());
        return true;
    }

    private void addField(JPanel form, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(label, gbc);

        field.setPreferredSize(new Dimension(320, 34));
        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Missing Information", JOptionPane.WARNING_MESSAGE);
    }
}
