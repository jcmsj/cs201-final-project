import javax.swing.JFrame;

import Style.Style;

public class App {
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
        final JFrame frame = new JFrame("Merge Sort Visualizer by 4Amigos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        final var director = new Director(App.createSampleBlocks());
        frame.add(director);
        java.awt.EventQueue.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}