package Block;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.JPanel;

/**
 * Layouts Block components in a Row and adds dividers at intervals based on
 * when split is set to 0, no dividers are added.
 * {@link Row#split}
 */
public class Row extends JPanel {
    public static final int X_GAP = 20;
    // public final int split;
    public final Block[] blocks;
    private int index = 0; // Determines which block would be painted
    private int counter = 1; // Determines when to place dividers

    private final LinkedList<Component> dividers = new LinkedList<>();

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
        putDivider();
        revalidate();
        index++;
        return true;
    }

    public void resetCounter() {
        counter = 0;
    }

    /*
     * Puts a divider if it should be
     */
    public boolean putDivider() {
        Integer last = splits.peekFirst();

        // last clause skips adding a divider after last item.
        if (last == null ||
                counter != last ||
                blocks.length == index) {
            counter++;
            return false;
        }
        System.out.println("Put divider at i=" + index);
        splits.removeFirst();
        addDivider();
        resetCounter();
        counter++;
        return true;
    }

    public boolean removeDivider() {
        counter--;
        if (counter == 0) {
            var div = dividers.pollLast();
            System.out.println("Removing divider beside index=" + index + " x=" + div.getX());
            remove(div);
            if (splits.peekLast() != null) {
                // Reset
                counter = splits.pollLast() - 1;
            }
        }
        return true;
    }

    /**
     * @return whether a block was removed from painting
     */
    public boolean undo() {
        int lastIndex = getComponentCount() - 1;

        if (index <= 0 || index > blocks.length || lastIndex < 0)
            return false;

        var last = getComponent(lastIndex);
        if (!(last instanceof Block)) {
            removeDivider();
        }
        index--;
        System.out.println("Removing " + blocks[index]);
        remove(blocks[index]);
        EventQueue.invokeLater(() -> {
            revalidate();
            repaint();
        });
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
        dividers.offer(d);
        return d;
    }

    public void revealAll() {
        while (next()) {
            // pass
        }
    }

    public void hideAll() {
        while (undo()) {
            // pass
        }
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