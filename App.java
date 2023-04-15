package Final;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Merge Sort Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        int baseWidth = 15;
        // odd size
        int[] ints = { 4, 1, 3, 2, 6, 9, 10 };
        // even size
        // int[] ints = { 4, 1, 3, 2, 6, 9, 0, 5 };
        Block[] blocks = new Block[ints.length];
        // Todo: User input
        // Create blocks
        for (int i = 0; i < ints.length; i++) {
            blocks[i] = new Block(new Point((i + 1) * baseWidth, 20), ints[i]);
            panel.add(blocks[i]);
        }

        panel.addKeyListener(new KeyListener() {
            final int todos = blocks.length;
            // `split` Represents the size of the subarrays made by MergeSort.
            int split = (int) Math.round(todos / 2.0); // Must do float division
            Timer timer = new Timer();

            public void move(Block b, boolean right) {
                move(b, right, true);
            }

            public void move(Block b, boolean right, boolean down) {
                Point p = b.getLocation();
                int changeX = (int) ((p.getX() + (right ? 1 : -1) * baseWidth * split));
                int changeY = (int) p.getY() + (down ? 1 : -1) * b.getHeight();
                b.setLocation(changeX, changeY);
                panel.repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // Activate by pressing right arrow key
                if (e.getKeyCode() != KeyEvent.VK_RIGHT)
                    return;

                if (split > 0) { // Split step
                    TimerTask task = new TimerTask() {
                        int done = 0;
                        int index = 0;
                        boolean right = false;

                        @Override
                        public void run() {
                            if (done < todos) {
                                // Reset `index` when it reaches `split`
                                if (index >= split) {
                                    index = 0;
                                    // Also negate the direction to move the newly split blocks away from each other
                                    right = !right;
                                }
                                move(blocks[done++], right);
                                index++;
                            } else {
                                this.cancel(); // Stop
                                split /= 2;
                            }
                        }
                    };

                    // To move a block one at a time, run `task` at an interval.
                    timer.scheduleAtFixedRate(task, 0, 300);
                } else { // Merge step
                    // todo
                    // Note: blocks now move upward
                }

                panel.repaint();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // Pass
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Pass
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}