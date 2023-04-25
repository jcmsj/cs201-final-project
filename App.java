import javax.swing.JFrame;
import Style.Style;

public class App extends JFrame {
    public App() {
        super("Merge Sort Visualizer by 4Amigos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public static Block[] createSampleBlocks() {
        // odd size 7
        int[] ints = { 4, 1, 3, 2, 6, 9, 10 };
        // odd size 9
        // int[] ints = { 4, 1, 3, 11, 2, 6, 0, 9, 10 };
        // even size
        //int[] ints = { 4, 1, 3, 2, 6, 9, 0, 5 };
        // many even 14
        //int[] ints = { 4, 1, 27, 3, 2, 19, 6, 9, 36, 0, 5, 17,13, 15, 18, 35 };
        Block[] blocks = new Block[ints.length];

        for (int i = 0; i < ints.length; i++) {
            blocks[i] = new Block(ints[i]);
        }

        return blocks;
    }

    public static void main(String[] args) {
        Style.init();
        final App app = new App();
        final var director = new Director(App.createSampleBlocks());
        app.add(director);
        java.awt.EventQueue.invokeLater(() -> {
            app.setVisible(true);;
        });
    }
}