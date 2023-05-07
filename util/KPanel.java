package util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

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
        this.image = Loader.imageIcon(path).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static EmptyBorder squareBorder(int side) {
        return new EmptyBorder(side, side, side, side);
    }

    public static CompoundBorder paddedBorder(Color c, int thickness, int padTop, int padLeft, int padBot,
            int padRight) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(c, thickness),
                BorderFactory.createEmptyBorder(padTop, padLeft, padBot, padRight));
    }

    public static CompoundBorder paddedBorder(Color c, int thickness, int pad) {
        return paddedBorder(c, thickness, pad, pad, pad, pad);
    }
}