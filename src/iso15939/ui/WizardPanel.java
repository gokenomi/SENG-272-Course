package iso15939.ui;

import javax.swing.JPanel;

public abstract class WizardPanel extends JPanel {
    public void beforeShow() {
    }

    public boolean beforeNext() {
        return true;
    }
}
