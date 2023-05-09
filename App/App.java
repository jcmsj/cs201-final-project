package App;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import Style.Style;

public class App extends JFrame {
    private MainPanel mainPanel;
    private JScrollPane scroll;
    public App() {
        super("Sort It Out: A Merge Sort Visualizer by Quatro Amigos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        mainPanel = new MainPanel();
        scroll = new JScrollPane(mainPanel);
        add(scroll);
    }

    public static void main(String[] args) {
        Style.init();
        EventQueue.invokeLater(App::new);
    }
}