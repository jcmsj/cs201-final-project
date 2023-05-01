import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Style.Style;
import util.On;
import Block.Block;
import Block.Editor;
import Directors.BlockDirector;
import Directors.Fader;
public class App extends JFrame {
    private static float BLOCK_FONT_SIZE = 30f;
    private Editor editor;
    enum MODE {
        AUTO,
        SEMI,
        MANUAL,
    }
    MODE mode = MODE.SEMI;
    private JPanel director;
    Consumer<int[]> consumer = new Consumer<int[]>() {
        @Override
        public void accept(int[] ints) {
            remove(editor);
            director = makeEditor(mode, ints);
            On.press(KeyEvent.VK_ESCAPE, director, ev -> {
                remove(director);
                addEditor();
            });
            add(director);
            repaint();
            revalidate();
            // Important: Focus the Component
            EventQueue.invokeLater(() -> {
                director.requestFocusInWindow();
            });
        }
    };
    
    public App() {
        super("Merge Sort Visualizer by 4Amigos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        addEditor();
    }

    public void addEditor() {
        if (editor == null) {
            editor = new Editor();
        }
        add(editor);
        editor.setConsumer(consumer);
        repaint();
        revalidate();
    }
    public JPanel makeEditor(MODE m, int[] ints) {
        switch (m) {
            case MANUAL:
                return new BlockDirector(ints);
            case SEMI:
                return new Fader(ints);
            default:
                // TODO
                break;
        }
        return null;
    }

    public static Block[] createSampleBlocks() {
        return Block.asBlocks(createSampleInputs(), BLOCK_FONT_SIZE);
    }
    public static int[] createSampleInputs() {
         // odd size 7
        //int[] ints = { 4, 1, 3, 2, 6, 9, 10 };
        // odd size 9
        //int[] ints = { 4, 1, 3, 11, 2, 6, 0, 9, 10 };
        // even size 8
        //int[] ints = { 4, 1, 3, 11, 2, 6, 0, 9 };
        int[] ints = { 5, 3, 4, 5 };
        // even size
        //int[] ints = { 4, 1, 3, 2, 6, 9, 0, 5 };
        // many even 14
        //int[] ints = { 4, 1, 27, 3, 2, 19, 6, 9, 36, 0, 5, 17,13, 15, 18, 35 };

        return ints;
    }

    public static void main(String[] args) {
        Style.init();
        final App app = new App();
        java.awt.EventQueue.invokeLater(() -> {
            app.setVisible(true);
        });
    }
}