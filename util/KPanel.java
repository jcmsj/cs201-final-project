package util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Adds some convenience methods to JPanel
 * K comes after J. Hence, KPanel
 */
public class KPanel extends JPanel {
    public Image image = null; 

    public void onPress(int keyCode, Consumer<KeyEvent> cb) {
        On.press(keyCode, this, cb);
    }

    public KPanel(String path) {
        this.image = new ImageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
        }
    }
}