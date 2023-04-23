package util;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

/**
 * Contains convenience functions for adding listeners
 */
public abstract class On implements Consumer<KeyEvent> {
    public static void press(int keyCode, Component target, Consumer<KeyEvent> cb ) {
        target.addKeyListener(press(keyCode, cb));
    }
    public static PressListener press(int keyCode, Consumer<KeyEvent> cb ) {
        return new PressListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != keyCode)
                    return;
                cb.accept(e);
            }
        };
    }
}