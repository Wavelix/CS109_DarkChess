import view.StartFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartFrame mainFrame = new StartFrame();
            mainFrame.setVisible(true);
        });

    }
}
