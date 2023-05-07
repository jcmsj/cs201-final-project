package Block;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import Style.Style;

/* A movable block with a number */
public class Block extends Box {
    public static float DEFAULT_FONT_SIZE = 40f;
    public int value;
    static final int side = 15;
    public Color shownColor;
    public BlockLabel label;
    public final static Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    public final static Border greenB = BorderFactory.createLineBorder(Color.green, 3);
    public final static Border redB = BorderFactory.createLineBorder(Style.LIGHT_RED, 3);

    public enum STATE {
        INVI,
        SHOWN,
        DIMMED
    }

    public STATE state = STATE.SHOWN;

    public Block(int value) {
        this(value, DEFAULT_FONT_SIZE);
    }

    public Block(int value, float fontSize) {
        this.value = value;
        label = new BlockLabel(value, fontSize);
        add(label);
        shownColor = label.getForeground();
        setBorder(DEFAULT_BORDER);
    }

    public void hideMe() {
        state = STATE.INVI;
        label.setForeground(Style.DARK_RED);
    }

    public void setState(STATE state) {
        switch (state) {
            case DIMMED:
                dim();
                break;
            case INVI:
                hideMe();
                break;
            case SHOWN:
                showMe();
                break;
            default:
                System.out.println("Unhandled state=" + state);
                break;
        }
    }

    public void nextState() {
        switch (state) {
            case DIMMED:
                // Pass
                break;
            case INVI:
                setState(STATE.SHOWN);
                break;
            case SHOWN:
                setState(STATE.DIMMED);
                break;
            default:
                break;
        }
    }

    public void prevState() {
        switch (state) {
            case INVI:
                // Pass
                break;
            case DIMMED:
                setState(STATE.SHOWN);
                break;
            case SHOWN:
                setState(STATE.INVI);
                break;
            default:
                break;
        }
    }


    public void useGreenBorder() {
        setBorder(greenB);
        repaint();
    }

    public void useRedBorder() {
        setBorder(redB);
        repaint();
    }

    public void useDefaultBorder() {
        setBorder(DEFAULT_BORDER);
        repaint();
    }
    public void dim() {
        state = STATE.DIMMED;
        label.setForeground(Style.reddish2);
    }

    public void showMe() {
        state = STATE.SHOWN;
        label.setForeground(shownColor);
    }

    /**
     * For debugging
     */
    public String toString() {
        return "(v=" + value + ", x=" + getX() + ", y=" + getY() + ")";
    }

    /**
     * Creates a new `Block` with the same value.
     * 
     * @implNote does not keep other state (E.g. position) unlike with
     *           {@link Object#clone}
     */
    public Block dup() {
        return new Block(value);
    }

    /**
     * Similar to {@link java.util.Arrays#copyOfRange} but uses {@link Block#dup}
     * 
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

    public static Block[] asBlocks(int[] ints, float fontSize) {
        Block[] blocks = new Block[ints.length];
        for (int i = 0; i < ints.length; i++) {
            blocks[i] = new Block(ints[i], fontSize);
        }
        return blocks;
    }
}