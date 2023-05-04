import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.JPanel;

import Block.Editor;
import Block.Editor.ACTIONS;
import Directors.AutoFader;
import Directors.BlockFader;
import Directors.Fader;
import util.KPanel;
import util.On;

public class MainPanel extends JPanel {
    private Editor editor;
    static final int DEFAULT_BORDER_SIZE = 30;
    ANIM_MODE mode = ANIM_MODE.SEMI;
    private JPanel director;
    public JPanel shown;
    private Consumer<int[]> consumer = new Consumer<int[]>() {
        @Override
        public void accept(int[] ints) {
            remove(editor);
            shown = director = makeEditor(mode, ints);
            On.press(KeyEvent.VK_ESCAPE, director, ev -> {
                remove(director);
                addEditor();
            });
            add(director);
            repaint();
            revalidate();
            // Important: Focus the Director
            EventQueue.invokeLater(() -> {
                director.requestFocusInWindow();
            });
        }
    };
    
    public MainPanel() {
        setBorder(KPanel.squareBorder(DEFAULT_BORDER_SIZE));
        editor = new Editor();
        editor.setConsumer(consumer);
        On.press(KeyEvent.VK_ENTER, editor, ev -> {
            editor.doAction(ACTIONS.PLAY);
        });
        addEditor();
    }

    public void setMode(ANIM_MODE m) {
        mode = m;
    }
    
    public void addEditor() {
        shown = editor;
        add(editor);
        repaint();
        revalidate();
    }
    public JPanel makeEditor(ANIM_MODE m, int[] ints) {
        switch (m) {
            case MANUAL:
                return new BlockFader(ints);
            case SEMI:
                return new Fader(ints);
            default:
                return new AutoFader(ints);
        }
    }
}