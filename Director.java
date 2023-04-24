import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
     * 1. Div by 2 but change 0 to 1.
     * 2. Must round the float division for correct splitting
     * where the higher count is on the left side
     * E.g. for 5 elements, it should be split into 3 2 not 2 3
     */
    public int calcSplit(int n) {
        return Math.max(n/2, 1);
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
    boolean shouldMerge = false;

    public void mergeStep() {
        if (split <= 0) {
            throw new IllegalArgumentException("split must be > 0, but got " + split);
        }
        System.out.println("Merge by " + split);
        ArrayList<Block> done = new ArrayList<>(blocks.length);
        int todo = 0;
        while (todo < blocks.length) {
            // Split step in the actual merge sort
            final int offset = Math.min(todo + split, blocks.length);
            Block[] left = Arrays.copyOfRange(blocks, todo, offset);
            final int nextOffset = Math.min(offset + split, blocks.length);
            Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            Block[] sorted = new Block[left.length + right.length];
            merge(left, right, sorted);
            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                // Important: duplicate block
                done.add(b.dup());
            }
            todo += sorted.length;
        }

        // Sync block positions
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = done.get(i);
        }
        split *= 2; // Increase now
        final Row r = new Row(blocks, split);
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

    boolean skipMid = true;

    public void mergeSort() {
        if (shouldMerge) {
            if (skipMid) {
                history.removeLast();
                skipMid = false;
            }
            mergeStep();
            return;
        }
        // Merge step starts next call
        splitStep();
    }

    /**
     * Makes a true clone of the indices
     */
    public LinkedList<Integer> dup(LinkedList<Integer> indices) {
        LinkedList<Integer> copy = new LinkedList<>();
        for (var index : indices) {
            copy.add(index);
        }
        return copy;
    }

    LinkedList<LinkedList<Integer>> history = new LinkedList<>();

    public boolean exactlyOne(LinkedList<Integer> ints, int n) {
        int count = 0;
        for (Integer x : ints) {
            count += x.compareTo(n) == 0 ? 1 : 0;
        }

        return count == 1;
    }

    public LinkedList<Integer> calcSplits() {
        LinkedList<Integer> indices = new LinkedList<>();
        var last = history.peekLast();
        if (last == null) {
            indices = Row.fill(blocks.length, 1); // returns [size]
        } else if (last.stream().allMatch(t -> t == 2) || exactlyOne(last, 1)) {
            indices = Row.fill(1, blocks.length);
            shouldMerge = true;
        } else {
            // returns size with the remainders added
            // E.g. split [9] becomes [4,5]
            // E.g. split [4,5] becomes [2,2] [2,3]
            last = dup(last);
            Integer target = last.peekFirst();
            while (target != null) {
                if (target == 3) {
                    indices.add(2);
                    indices.add(1);
                } else {
                    int n = calcSplit(target);
                    int i = n;
                    System.out.println("Split by " + n);
                    while (i <= target) {
                        int x = n;
                        //At the last iteration, there may be a leftover element. So add that
                        if (i + 1 == target) {
                            x++;
                        }
                        //Add `x` not the index `i`, Row.java handles the indexing for us
                        indices.add(x);
                        i += x;
                    }
                }
                last.removeFirst();
                target = last.peekFirst();
            }
        }
        history.add(indices);
        System.out.println(indices);
        return dup(indices);
    }

    public void splitStep() {
        final Row row = new Row(blocks, calcSplits());
        split = calcSplit(split);
        add(row);
        animRow(row, null);
    }
}