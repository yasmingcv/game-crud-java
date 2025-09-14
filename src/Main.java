import controller.GameController;
import view.MainView;

import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController controller = new GameController();
            MainView view = new MainView(controller);
            view.setVisible(true);
        });
    }
}