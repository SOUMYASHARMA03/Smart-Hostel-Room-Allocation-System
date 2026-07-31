import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import ui.LoginFrame;

public class Main {

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Fall back to the default cross-platform look and feel
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
