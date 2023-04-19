import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class App {

    public static Block[] addSampleBlocks(JPanel j) {
        final int baseWidth = 15;
        // odd size
        int[] ints = { 4, 1, 3, 2, 6, 9, 10 };
        // even size
        //[] ints = { 4, 1, 3, 2, 6, 9, 0, 5 };
        Block[] blocks = new Block[ints.length];
        final var color = new Color(10,50,50,64);
        for (int i = 0; i < ints.length; i++) {
            blocks[i] = new Block(new Point((i + 1) * baseWidth, 100), ints[i]);
            blocks[i].setBackground(color);
            j.add(blocks[i]);
        }

        return blocks;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Merge Sort Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        int baseWidth = 15;
        // Todo: User input
        // Create blocks
        //MergeBlocks merger = new MergeBlocks();
        //merger.setFocusable(true);
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));
        var blocks = addSampleBlocks(panel);
        frame.getContentPane().add(panel);
        panel.addKeyListener(new SplitListener(blocks, baseWidth, panel));
        frame.setVisible(true);
    }
}