import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameScreen ui = new GameScreen();
            ui.setVisible(true);
        });
    }
}