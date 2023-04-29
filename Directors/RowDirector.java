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
import Style.Style;
import util.Animator;
import util.KPanel;

/**
 * Controls a row to animate in/out all its blocks in one activation.
 */
public class RowDirector extends JPanel implements DirectorLike {
    static final int DEFAULT_BORDER_SIZE = 30;
    public final Animator anim = new Animator();
    protected final Block[] blocks;
    protected LinkedList<LinkedList<Integer>> history = new LinkedList<>();
    protected LinkedList<Row> rows = new LinkedList<>();
    private int borderSize;

    public RowDirector(int[] ints, float fontSize) {
        this(Block.asBlocks(ints, fontSize));
    }

    public RowDirector(int[] ints) {
        this(Block.asBlocks(ints, Block.DEFAULT_FONT_SIZE));
    }
    public RowDirector(Block[] blocks, int borderSize) {
        // super("./assets/<your-image-file>");
        // use BoxLayout as Rows will be added vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.borderSize = borderSize;
        this.blocks = blocks;
        setBorder(KPanel.squareBorder(this.borderSize));
        setFocusable(true);
        setBackground(Style.wine);
        setVisible(true);
        // Attach listeners at the end
        addListeners();
    
    }
    public RowDirector(Block[] blocks) {
        this(blocks, DEFAULT_BORDER_SIZE);
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
            if (r.undo()) {

            } else {
                t.cancel();
                syncPositions(r.blocks);
                split /=2;
                remove(r);
            }
        }, anim.interval);
    }

    /* Animate in each block for the row */
    public void animateRow(Row r, Runnable onEnd) {
        anim.every(t -> {
            if (r.next()) {

            } else {
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
        int todo = 0;
        while (todo < blocks.length) {
            // Split step in the actual merge sort
            final int offset = Math.min(todo + split, blocks.length);
            Block[] left = Arrays.copyOfRange(blocks, todo, offset);
            final int nextOffset = Math.min(offset + split, blocks.length);
            Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            Block[] sorted = new Block[left.length + right.length];
            Aqua.merge(left, right, sorted);
            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                // Important: duplicate block
                done.add(b.dup());
            }
            todo += sorted.length;
        }

        syncPositions(done);
        split *= 2; // Increase now
        final Row r = new Row(blocks, split);
        add(r);
        animateRow(r, null);
    }

    public void syncPositions(ArrayList<Block> newPos) {
        // Sync block positions
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = newPos.get(i);
        }
    }
    public void syncPositions(Block[] newPos) {
        // Sync block positions
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = newPos[i];
        }
    }

    public void mergeSort() {
        mergeStep();
    }

    public LinkedList<Integer> calcSplits() {
        LinkedList<Integer> indices = new LinkedList<>();
        var last = history.peekLast();
        if (last == null) {
            // return [size]
            indices = Row.fill(blocks.length, 1);
        } else if (Aqua.allTwos(last) || Aqua.exactlyOne(last, 1)) {
            /*
             * 2nd to the last split check
             * when array is odd-sized, need to check if the last point in history has
             * exactly one element with the value 1.
             * For even case, check if all are 2s
             */

            // Last split state is simple, every element is in a one-sized array
            indices = Row.fill(1, blocks.length);
            shouldMerge = true;
        } else {
            /*
             * returns size with the remainders added
             * E.g. split [9] becomes [4,5]
             * E.g. split [4,5] becomes [2,2] [2,3]
             * to do that here, further split the last list of splits done
             */
            last = Aqua.dup(last);
            Integer target = last.peekFirst();
            while (target != null) {
                // For odd-sized elements, there'd always be a case where the target becomes 3.
                // add 2 & 3 if so.
                if (target == 3) {
                    indices.add(2);
                    indices.add(1);
                } else {
                    int n = calcSplit(target);
                    int i = n;
                    System.out.println("Split by " + n);
                    while (i <= target) {
                        int x = n;
                        // At the last iteration, there may be a leftover element. So add that
                        if (i + 1 == target) {
                            x++;
                        }
                        // Add `x` not the index `i`, Row.java handles the indexing for us
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
        return Aqua.dup(indices);
    }

    public void splitStep() {
        final Row row = new Row(blocks, calcSplits());
        split = calcSplit(split);
        add(row);
        animateRow(row, null);
    }

    /**
     * Calls {@link JPanel#add} then adds a reference of `row` in {@link RowDirector#rows}
     */
    public Row add(Row row) {
        add((Component) row);
        rows.add(row);
        return row;
    }

    /**
     * Calls {@link JPanel#remove} then removes `row`'s reference in {@link RowDirector#rows}
     */
    public Row remove(Row row) {
        remove((Component) row);
        rows.remove(row);
        return row;
    }
}