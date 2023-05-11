package Directors;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import Block.Block;
import Block.Row;
import util.Animator;

/**
 * Controls a row to animate in/out all its blocks in one activation.
 */
public class RowDirector extends JPanel implements DirectorLike {
    public Animator anim = new Animator();
    protected final Block[] blocks;
    protected LinkedList<LinkedList<Integer>> history = new LinkedList<>();
    protected LinkedList<Row> rows = new LinkedList<>();

    public RowDirector(int[] ints, float fontSize) {
        this(Block.asBlocks(ints, fontSize));
    }

    public RowDirector(int[] ints) {
        this(Block.asBlocks(ints, Block.DEFAULT_FONT_SIZE));
    }

    public RowDirector(Block[] blocks) {
        // use BoxLayout as Rows will be added vertically
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.blocks = blocks;
        setFocusable(true);
        setVisible(true);
        // Attach listeners at the end
        addListeners();
    }

    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this,
                t -> mergeSort());

        // Activate by pressing left arrow key
        anim.onPress(KeyEvent.VK_LEFT, this,
                t -> undoLastRow());
    }

    public void undoLastRow() {
        Row r = rows.pollLast();
        if (r == null) {
            System.out.println("Empty rows");
            return;
        }
        anim.every(t -> {
            if (!r.undo()) {
                t.cancel();
                Aqua.syncPositions(r.blocks, blocks);
                split /= 2;
                remove(r);
            }
        }, anim.interval);
    }

    /* Animate in each block for the row */
    public void animateRow(Row r, Runnable onEnd) {
        anim.every(t -> {
            if (!r.next()) {
                t.cancel();
                if (onEnd != null)
                    onEnd.run();
            }
        }, anim.interval);
    }

    int split = 0;
    boolean shouldMerge = false;

    public void mergeStep() {
        if (split <= 0) {
            split = 1;
        } else {
            Aqua.mergeStep(blocks, split);
            split *= 2; // Increase now
        }
        final Row r = new Row(blocks, split);
        add(r);
        animateRow(r, null);
    }

    public void mergeSort() {
        if (split >= blocks.length) {
            System.out.println("Sorted!");
            return;
        }
        mergeStep();
    }

    /**
     * Calls {@link JPanel#add} then adds a reference of `row` in
     * {@link RowDirector#rows}
     */
    public Row add(Row row) {
        add((Component) row);
        rows.add(row);
        return row;
    }

    /**
     * Calls {@link JPanel#remove} then removes `row`'s reference in
     * {@link RowDirector#rows}
     */
    public Row remove(Row row) {
        remove((Component) row);
        rows.remove(row);
        return row;
    }
}