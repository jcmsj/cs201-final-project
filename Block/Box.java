package Block;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import util.KPanel;
import util.Loader;

/* 
 * Contains the look of a Block
 */
public class Box extends KPanel {
    final static Image focusedImage = Loader.imageIcon("assets/f-block.png").getImage();
    Image unfocusedImage;
    public static float DEFAULT_FONT_SIZE = 40f;
    public static float GLOBAL_FONT_SIZE = DEFAULT_FONT_SIZE;
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
    
    public void updateFont() {
        setFont(getFont().deriveFont(GLOBAL_FONT_SIZE));
    }

    public Box() {
        super("assets/block.png");
        unfocusedImage = image;
        addMouseListener(hoverer);
    }
}