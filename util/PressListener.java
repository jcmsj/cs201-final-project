package util;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * When you only need the KeyPressed event
 */
public abstract class PressListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
        // Pass
    }
    @Override
    public void keyReleased(KeyEvent e) {
        // Pass
    }
}