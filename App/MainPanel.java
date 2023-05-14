package App;

import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import Block.Block;
import Block.Editor;
import Directors.AutoFader;
import Directors.BlockFader;
import Directors.Fader;
import Directors.RowFader;
import Style.Style;
import util.ANIM_MODE;
import util.CellRenderer;
import util.KPanel;
import util.On;

public class MainPanel extends KPanel {
    public Editor editor;
    static final int DEFAULT_BORDER_SIZE = 30;
    ANIM_MODE mode = ANIM_MODE.ALL;
    private JPanel director;
    public JPanel shown;

    public void onSubmit(int[] ints) {
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
        EventQueue.invokeLater(director::requestFocusInWindow);
    }

    public static class ModeButton extends JRadioButton {
        public ModeButton(String text, ANIM_MODE m) {
            super(text);
            setBorder(KPanel.squareBorder(5));
            setActionCommand(m.toString());
            setFont(getFont().deriveFont(20f));
        }
    }

    public class ModePanel extends KPanel {
        public ModePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(CENTER_ALIGNMENT);
            setBorder(KPanel.paddedBorder(Style.yellowish.darker(), 1, 8));
            JLabel modeTitle = new JLabel("Animation mode");
            modeTitle.setFont(modeTitle.getFont().deriveFont(40f));
            modeTitle.setAlignmentX(CENTER_ALIGNMENT);
            add(modeTitle);
            DefaultListModel<ANIM_MODE> model = new DefaultListModel<>();
            model.addElement(ANIM_MODE.ALL);
            model.addElement(ANIM_MODE.ROW);
            model.addElement(ANIM_MODE.ROW_OLD);
            model.addElement(ANIM_MODE.BLOCK);
            JList<ANIM_MODE> list = new JList<>(model);
            list.setCellRenderer(new CellRenderer());
            list.setAlignmentX(CENTER_ALIGNMENT);

            // Updates mode field
            list.addListSelectionListener(l -> {
                mode = list.getSelectedValue();
            });
            list.setSelectedIndex(0);
            add(list);
            preventResize();
        }
    }

    public MainPanel() {
        super("assets/stage.png");
        setBorder(KPanel.squareBorder(DEFAULT_BORDER_SIZE));
        editor = new Editor();
        editor.add(Box.createVerticalStrut(50));
        var modePanel = new ModePanel();
        modePanel.add(new SizeChanger());
        modePanel.add(Box.createVerticalStrut(10));
        editor.add(modePanel);
        editor.setConsumer(this::onSubmit);
        addEditor();
    }

    public class SizeChanger extends JPanel {
        public final JTextField input;
        public SizeChanger() {
            var label = new JLabel("Block Font size: ");
            label.setFont(getFont().deriveFont(30f));
            input = new JTextField("" + (int) Block.GLOBAL_FONT_SIZE, 3);
            input.setBorder(KPanel.paddedBorder(getBackground(), 1, 3));
            input.setFont(getFont().deriveFont(30f));
            input.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    // pass
                }
                @Override
                public void focusLost(FocusEvent e) {
                    int newSize = Integer.parseInt(input.getText().trim());
                    if (newSize > 0)  {
                        Block.GLOBAL_FONT_SIZE = newSize;
                    } else {
                        input.setText("" + (int) Block.GLOBAL_FONT_SIZE);
                    }
                }
            });
            add(label);
            add(input);

        }
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
            case BLOCK:
                return new BlockFader(ints);
            case ROW_OLD:
                return new Fader(ints);
            case ROW:
                return new RowFader(ints);
            default:
                return new AutoFader(ints);
        }
    }
}