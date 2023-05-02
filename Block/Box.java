package Block;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import util.KPanel;

/* 
 * Contains the look of a Block
 */
public class Box extends KPanel {
    final static Image focusedImage = new ImageIcon("./assets/f-block.png").getImage();
    Image unfocusedImage;

    public MouseListener hoverer = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Pass
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // Pass
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Pass
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            hover();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            unhover();
        }
    };
    public void hover() {
        image = focusedImage;
        repaint();
    }

    public void unhover() {
        image = unfocusedImage;
        repaint();
    }
    
    public Box() {
        super("./assets/block.png");
        unfocusedImage = image;
        addMouseListener(hoverer);
    }
}