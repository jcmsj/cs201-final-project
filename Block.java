import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Style.Style;
import util.KPanel;

/* A movable block with a number */
public class Block extends KPanel {
    public static final float DEFAULT_FONT_SIZE = 30f;
    public final int  value;
    static final int side = 15;

    public Block(int value) {
        this(value, DEFAULT_FONT_SIZE);
    }

    public Block(int value, float fontSize) {
        super("./assets/block.png");
        this.value = value;
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setOpaque(false);
        JLabel label = new JLabel("" + value);
        //Resize font to 40;
        label.setFont(getFont().deriveFont(fontSize));
        label.setForeground(Style.reddish);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(side, side, side, side));
        panel.add(label);
        add(panel);
    }
    /**
     * For debugging
     */
    public String toString() {
        return "(v=" + value + ", x=" + getX() + ", y=" + getY() + ")";
    }

    /**
     * Creates a new `Block` with the same value.
     * @implNote does not keep other state (E.g. position) unlike with {@link Object#clone}
     */
    public Block dup() {
        return new Block(value);
    }

    /**
     * Similar to {@link java.util.Arrays#copyOfRange} but uses {@link Block#dup}
     * @see Block#dup
     */
    public static Block[] copyOfRange(Block[] original, int from, int to) throws IllegalArgumentException {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);

        newLength = Math.min(original.length - from, newLength);
        Block[] copy = new Block[newLength];
        
        for (int i = 0; i < newLength; i++) {
            copy[i] = original[i].dup();
        }

        return copy;
    }

    public static Block[] copy(Block[] blocks) {
        var copy = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            copy[i] = blocks[i].dup();
        }
        return copy;
    }
}