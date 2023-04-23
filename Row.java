import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JPanel;
import Style.Style;

/**
 * Layouts Block components in a Row and adds dividers at intervals based on
 * when split is set to 0, no dividers are added.
 * {@link Row#split}
 */
public class Row extends JPanel {
    public static final int X_GAP = 10;
    public final int split;
    public final Block[] blocks;
    private int index = 0;
    private LinkedList<Component> dividers = new LinkedList<>();

    public int getIndex() {
        return index;
    }

    /**
     * Determines the width of the divider
     */
    public final int xGAP;

    /**
     * @implNote Creates a new copy of blocks using {@link Block#copy}
     */
    public Row(Block[] blocks, int split, int xGAP) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.xGAP = xGAP;
        this.split = split;
        if (split < 0) {
            throw new IllegalArgumentException("split must be nonnegative but got " + split);
        }
        // Duplicate blocks
        this.blocks = Block.copy(blocks);
        // Starts at 1 for easier comparison
        System.out.println("Split by " + split);
    }

    /**
     * @return whether a block was newly painted
     */
    public boolean next() {
        if (index >= blocks.length)
            return false;

        add(blocks[index]);
        // add divider
        // 2nd clause skips adding a divider after last item.
        if ((index + 1) % split == 0 && blocks.length != index) {
            System.out.println("split at i=" + index);
            addDivider();
        }
        revalidate();
        index++;
        return true;
    }

    /**
     * @return whether a block was removed from painting
     */
    public boolean undo() {
        if (index >= blocks.length)
            return false;

        // Remove divider if exists
        if ((index % split == 0 && blocks.length != index)) {
            remove(dividers.pop());
        }
        remove(blocks[index--]);
        revalidate();
        return true;
    }

    /**
     * Adds a divider that is {@link Row#xGAP} long
     * and a reference in dividers
     * 
     * @see Row#add
     */
    public Component addDivider() {
        var d = add(Box.createHorizontalStrut(xGAP));
        dividers.push(d);
        return d;
    }

    /**
     * Creates a Row with the {@link Row#X_GAP} as {@link Row#xGAP}
     */
    public Row(Block[] blocks, int split) {
        this(blocks, split, Row.X_GAP);
    }

    /**
     * Creates a Row without dividers and default {@link Row#xGAP}
     */
    public Row(Block[] blocks) {
        this(blocks, blocks.length, Row.X_GAP);
    }

    public static void main(String[] args) {
        Style.init();
        final JFrame frame = new JFrame("Merge Sort Visualizer by 4Amigos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Maximize screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        final var director = new Director(App.createSampleBlocks());
        frame.add(director);
        java.awt.EventQueue.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
