import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Animator;

public class Director extends JPanel {
    private final Block[] blocks;
    public final Animator anim = new Animator();

    public Director(Block[] blocks) {
        // use BoxLayout as Rows will be added vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setFocusable(true);
        setVisible(true);
        this.blocks = blocks;
        split = blocks.length;
        // Activate by pressing right arrow key
        anim.onPress(
                KeyEvent.VK_RIGHT,
                this,
                t -> mergeSort());
    }

    public int calcSplit() {
        return calcSplit(blocks.length);
    }

    /**
     * Must round the float division for correct splitting.
     * where the higher count is in the left side
     * E.g. for 5 elements
     * it should be split into 3 2 not 2 3
     */
    public int calcSplit(int n) {
        return n / 2;
    }

    public void animRow(Row r, Runnable onEnd) {
        anim.every(t -> {
            if (r.next()) {

            } else {
                t.cancel();
                if (onEnd != null)
                    onEnd.run();
            }
        }, anim.interval);
    }

    int split;
    int me = 1;
    boolean shouldMerge = false;

    public void mergeStep() {
        System.out.println("Merge by " + me);
        int todo = 0;
        ArrayList<Block> done = new ArrayList<>(blocks.length);
        while (todo < blocks.length) {
            // Split step in the actual merge sort
            final int offset = Math.min(todo + me, blocks.length);
            Block[] left = Arrays.copyOfRange(blocks, todo, offset);
            final int nextOffset = Math.min(offset + me, blocks.length);
            Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            Block[] sorted = new Block[left.length + right.length];
            merge(left, right, sorted);
            System.out.println(
                "Left:\n" + Arrays.toString(left)
                + "\nRight:\n" + Arrays.toString(right)
                + "\nSorted:\n" + Arrays.toString(sorted)
            );
            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                // Important: duplicate block
                done.add(b.dup());
            }
            todo += sorted.length;
        }

        // Sync pos
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = done.get(i);
        }
        me *= 2; // Increase now
        var r = new Row(blocks, me);
        add(r);
        animRow(r, null);
    }
   /**
     * Repositions each block in `source`
     * based on the position at an index
     * then puts each to `target`
     */
   
    public void merge(Block[] left, Block[] right, Block[] target) {
        int i = 0, l = 0, r = 0; // indices
        // Check the conditions for merging
        // Important: Compare value field of blocks
        while (l < left.length && r < right.length) {
            target[i++] = left[l].value < right[r].value ? left[l++] : right[r++];
        }
        // Put the rest
        while (l < left.length) {
            target[i++] = left[l++];
        }
        while (r < right.length) {
            target[i++] = right[r++];
        }
    }

    public void mergeSort() {
        if (shouldMerge) {
            mergeStep();
            return;
        }
        // Merge step starts next call
        if (split == 1)
            shouldMerge = true;
        splitStep();
    }

    public void splitStep() {
        final Row row = new Row(blocks, split);
        split = calcSplit(split);
        add(row);
        animRow(row, null);
    }
}