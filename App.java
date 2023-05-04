import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import Style.Style;
import Block.Block;

public class App extends JFrame {
    private static float BLOCK_FONT_SIZE = 30f;
    private MainPanel mainPanel;
    private JScrollPane scroll;
    public App() {
        super("Sort It Out: A Merge Sort Visualizer by 4Amigos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        mainPanel = new MainPanel();
        scroll = new JScrollPane(mainPanel);
        add(scroll);
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