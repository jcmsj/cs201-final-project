import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Style.Style;
import util.On;
import Block.Block;

public class App extends JFrame {
    private static float BLOCK_FONT_SIZE = 30f;
    private MainPanel mainPanel;
    private Config config;
    private JScrollPane scroll;
    Runnable toggleSettings = () -> {
        if (config.isVisible()) {
            config.setVisible(false);
            EventQueue.invokeLater(() -> mainPanel.shown.requestFocusInWindow());
        } else {
            EventQueue.invokeLater(() -> config.requestFocusInWindow());
            config.setVisible(true);
        }
    };
    public App() {
        super("Merge Sort Visualizer by 4Amigos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        mainPanel = new MainPanel();
        config = new Config(getWidth() / 4, getHeight(), c -> {
            mainPanel.mode = c;            
        });
        config.setVisible(false);
        add(config);
        config.activator.addActionListener(e -> toggleSettings.run());
        add(config.activator);
        scroll = new JScrollPane(mainPanel);
        add(scroll);
        initHotkeys();
    }
    public void initHotkeys() {
        On.press(KeyEvent.VK_ESCAPE, config, e -> toggleSettings.run());
    }
    public static Block[] createSampleBlocks() {
        return Block.asBlocks(createSampleInputs(), BLOCK_FONT_SIZE);
    }

    public static int[] createSampleInputs() {
        // odd size 7
        // int[] ints = { 4, 1, 3, 2, 6, 9, 10 };
        // odd size 9
        // int[] ints = { 4, 1, 3, 11, 2, 6, 0, 9, 10 };
        // even size 8
        // int[] ints = { 4, 1, 3, 11, 2, 6, 0, 9 };
        int[] ints = { 5, 3, 4, 5 };
        // even size
        // int[] ints = { 4, 1, 3, 2, 6, 9, 0, 5 };
        // many even 14
        // int[] ints = { 4, 1, 27, 3, 2, 19, 6, 9, 36, 0, 5, 17,13, 15, 18, 35 };

        return ints;
    }

    public static void main(String[] args) {
        Style.init();
        EventQueue.invokeLater(App::new);
    }
}