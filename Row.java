import java.awt.Component;
import java.awt.FlowLayout;
import java.util.LinkedList;
import javax.swing.Box;
import javax.swing.JPanel;
import Block.Block;

/**
 * Layouts Block components in a Row and adds dividers at intervals based on
 * when split is set to 0, no dividers are added.
 * {@link Row#split}
 */
public class Row extends JPanel {
    public static final int X_GAP = 10;
    // public final int split;
    public final Block[] blocks;
    private int index = 0; // Determines which block would be painted
    private int counter = 1; // Determines when to place dividers

    private LinkedList<Component> dividers = new LinkedList<>();

    public int getIndex() {
        return index;
    }

    /**
     * Determines the width of the divider
     */
    public final int xGAP;
    private LinkedList<Integer> splits;

    /**
     * @implNote Creates a new copy of blocks using {@link Block#copy}
     */
    public Row(Block[] blocks, LinkedList<Integer> splits, int xGAP) {
        super(new FlowLayout(FlowLayout.CENTER));
        setOpaque(false);
        this.splits = splits;
        this.xGAP = xGAP;
        // Filter negative values
        splits.removeIf(it -> it < 0);
        this.blocks = Block.copy(blocks);
    }

    /**
     * Creates a Row with the {@link Row#X_GAP} as {@link Row#xGAP}
     */
    public Row(Block[] blocks, int split) {
        this(blocks, fill(split, blocks.length), Row.X_GAP);
    }

    public Row(Block[] blocks, LinkedList<Integer> splits) {
        this(blocks, splits, Row.X_GAP);
    }

    /**
     * Creates a Row without dividers and default {@link Row#xGAP}
     */
    public Row(Block[] blocks) {
        this(blocks, new LinkedList<>(), Row.X_GAP);
    }

    /**
     * @return whether a block was newly painted
     */
    public boolean next() {
        if (index >= blocks.length)
            return false;

        // add divider
        add(blocks[index]);
        Integer last = splits.peekFirst();
        putDivider(last);
        revalidate();
        counter++;
        index++;
        return true;
    }

    public void resetCounter() {
        counter = 0;
    }

    /* 
     * Puts a divider if it should be
     */
    public boolean putDivider(Integer last) {
        // last clause skips adding a divider after last item.
        if (last == null || 
            counter != last || 
            blocks.length == index)
            return false;
        System.out.println("Put divider at i=" + index);
        splits.removeFirst();
        addDivider();
        resetCounter();
        return true;
    }

    /**
     * @return whether a block was removed from painting
     */
    public boolean undo() {
        if (index >= blocks.length)
            return false;

        // Remove divider if exists
        Integer last = splits.peekFirst();
        if (last != null
                && counter == last
                && blocks.length != index) {
            System.out.println("split at i=" + index);
        }
        if ((index == splits.removeLast() && blocks.length != index)) {
            remove(dividers.pop());
        }
        remove(blocks[index]);
        counter--;
        index--;
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

    /*
     * Immediately fills this Row with Blocks having values `n`
     */
    public static LinkedList<Integer> fill(Integer n, int size) {
        LinkedList<Integer> r = new LinkedList<>();
        for (; size > 0; size--) {
            r.add(n);
        }
        return r;
    }
}