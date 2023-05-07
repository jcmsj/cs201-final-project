package Directors;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
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

    public int calcSplit() {
        return calcSplit(blocks.length);
    }

    /**
     * Div by 2 but change 0 to 1.
     */
    public static int calcSplit(int n) {
        return Math.max(n / 2, 1);
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
        if (split > blocks.length)
            return;

        if (split < 0) {
            throw new IllegalArgumentException("split must be > 0, but got " + split);
        }

        if (split == 0) {
            final Row r = new Row(blocks, 1);
            add(r);
            animateRow(r, null);
            split = 1;
            return;
        }
        System.out.println("Merge by " + split);
        ArrayList<Block> done = new ArrayList<>(blocks.length);
        while (done.size() < blocks.length) {
            // Split step in the actual merge sort
            int progress = done.size();
            final int offset = Math.min(progress + split, blocks.length);
            Block[] left = Arrays.copyOfRange(blocks, progress, offset);
            final int nextOffset = Math.min(offset + split, blocks.length);
            Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            Block[] sorted = new Block[left.length + right.length];
            Aqua.merge(left, right, sorted);
            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                // Important: duplicate block
                done.add(b.dup());
            }
        }

        Aqua.syncPositions(done, blocks);
        split *= 2; // Increase now
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