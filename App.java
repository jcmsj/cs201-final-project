package Final;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class App {

    public App() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Merge Sort Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        JPanel panel = new JPanel();
        panel.setFocusable(true);
        int baseWidth = 15;
        //odd size
        int[] ints = {4,1,3,2,6,9,10};
        //even size
        // int[] ints = {4,1,3,2,6,9,10,5};
        Block[] blocks = new Block[ints.length];
        //Todo: User input
        //Create blocks
        for (int i = 0; i < ints.length; i++) {
            blocks[i] = new Block(new Point((i+1) * baseWidth, 20), ints[i]);
            panel.add(blocks[i]);
        }
        
        panel.addKeyListener(new KeyListener() {
            final int todos = blocks.length;
            int split = (int) Math.round(todos/2.0); //Must do float division
            public void move(Block b, boolean right) {
                Point p = b.getLocation();
                int changeX = (int) ((p.getX() + (right ? 1:-1) * baseWidth * split));
                b.setLocation(changeX,(int) p.getY() + b.getHeight());
                panel.repaint();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_RIGHT)
                    return;
                
                if (split > 0) {  //Split step
                    int j = 0;
                    //System.out.println(split);
                    boolean right = false;
                    while(j < todos) {
                        for (int i = 0; i < split && j < todos; i++) {
                            move(blocks[j++], right);
                        }
                        right = !right;
                        /* for (int i = 0; i < split && j < todos; i++) {
                            move(blocks[j++], true);
                        } */
                    }
                    split/=2;
                } else { // Merge step
                    //todo
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