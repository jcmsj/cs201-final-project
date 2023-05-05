import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import Block.Editor;
import Block.ACTIONS;
import Directors.AutoFader;
import Directors.BlockFader;
import Directors.RowFader;
import Style.Style;
import util.ANIM_MODE;
import util.CellRenderer;
import util.KPanel;
import util.On;

public class MainPanel extends KPanel {
    public Editor editor;
    static final int DEFAULT_BORDER_SIZE = 30;
    ANIM_MODE mode = ANIM_MODE.AUTO;
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

    public static class ModeButton extends JRadioButton {
        public ModeButton(String text, ANIM_MODE m) {
            super(text);
            setBorder(KPanel.squareBorder(5));
            setActionCommand(m.toString());
            setFont(getFont().deriveFont(20f));
        }
    }

    public class ModePanel extends JPanel {
        public ModePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(CENTER_ALIGNMENT);
            setBorder(KPanel.paddedBorder(Style.yellowish.darker(), 1, 8));
            JLabel modeTitle = new JLabel("Animation mode");
            modeTitle.setFont(modeTitle.getFont().deriveFont(40f));
            modeTitle.setAlignmentX(CENTER_ALIGNMENT);
            add(modeTitle);
            DefaultListModel<ANIM_MODE> model = new DefaultListModel<>();
            model.addElement(ANIM_MODE.AUTO);
            model.addElement(ANIM_MODE.SEMI);
            model.addElement(ANIM_MODE.MANUAL);
            JList<ANIM_MODE> list = new JList<>(model);
            list.setCellRenderer(new CellRenderer());
            list.setAlignmentX(CENTER_ALIGNMENT);

            //Updates mode field
            list.addListSelectionListener(l -> {
                mode = list.getSelectedValue();
            });
            list.setSelectedIndex(0);
            add(list);
        }
    }

    public MainPanel() {
        super("./assets/stage.png");
        setBorder(KPanel.squareBorder(DEFAULT_BORDER_SIZE));
        editor = new Editor();
        editor.add(Box.createVerticalStrut(50));
        editor.add(new ModePanel());
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
                return new RowFader(ints);
            default:
                return new AutoFader(ints);
        }
    }
}