import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;

public class Animator extends JPanel implements KeyListener {
    private final Block[] blocks;
    private final int moveDistanceX;
    private final JPanel panel;
    private final LinkedList<ArrayList<Point>> history = new LinkedList<>();
    // `split` Represents the size of subarrays made by MergeSort for split step.
    private int split;
    // Same with `split` but for merge step.
    private int merge = 1;
    private final Timer timer = new Timer();
    private int intervalMS = 50; //in ms
    private boolean animating = false;
    public ArrayList<Point> blocksToLocation(Block[] blocks) {
        return Arrays.stream(blocks)
                .map(b -> b.getLocation())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Records the current location of all blocks
     */
    public boolean snapshot(Block[] blocks) {
        return history.add(blocksToLocation(blocks));
    }

    Animator(Block[] blocks, int moveDistanceX, int intervalMS, JPanel panel) {
        this.moveDistanceX = moveDistanceX;
        this.blocks = blocks;
        this.panel = panel;
        this.intervalMS = intervalMS;
        split = calcSplit(blocks);
    }

    // Must do float division
    public int calcSplit(Block[] blks) {
        return (int) Math.round(blks.length / 2.0);
    }

    public void move(Block b, boolean right) {
        move(b, right, true);
    }

    public void move(Block b, boolean right, boolean down) {
        move(b, right, down, split);
    }

    public void move(Block b, boolean right, boolean down, int _split) {
        int changeX = (int) ((b.getX() + (right ? 1 : -1) * moveDistanceX * _split));
        int changeY = (int) b.getY() + (down ? 1 : -1) * b.getHeight();
        b.setLocation(changeX, changeY);
        panel.repaint();
    }

    public void goSplit(Block[] blks) {
        snapshot(blks); // Record
        TimerTask task = new TimerTask() {
            int done = 0;
            int index = 0;
            boolean right = false;

            @Override
            public void run() {
                if (done < blks.length) {
                    // Reset `index` when it reaches `split`
                    if (index >= split) {
                        index = 0;
                        // Also negate the direction to move the newly split blocks away from each other
                        right = !right;
                    }
                    move(blks[done++], right);
                    index++;
                } else {
                    this.cancel(); // Stop
                    System.out.println("split " + split);
                    split /= 2;
                    animating = false;
                }
            }
        };

        // To move a block one at a time, run `task` at an interval.
        timer.scheduleAtFixedRate(task, 0, intervalMS);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Activate by pressing right arrow key
        if (animating || e.getKeyCode() != KeyEvent.VK_RIGHT )
            return;

        animating = true;

        if (split > 0) {
            goSplit(blocks);
            return;
        }
        goMerge(blocks);
    }

    public void goMerge(Block[] blks) {
        final ArrayList<Point> row = history.peekLast();
        if (row == null) {
            System.out.println("loop");
            {
                // Loop animation
                merge = 1;
                split = calcSplit(blks);
                goSplit(blks);
            }
            return;
        }
        System.out.println(row);

        int todo = 0;
        ArrayList<Block> done = new ArrayList<>(blks.length);
        System.out.println("merge size " + merge);
        while (todo < blks.length) {
            // Split step in the actual merge sort
            int offset = Math.min(todo + merge, blks.length);
            Block[] left = Arrays.copyOfRange(blocks, todo, offset);
            Block[] right = Arrays.copyOfRange(blocks, offset, Math.min(todo + merge * 2, blks.length));
            Block[] sorted = new Block[left.length + right.length];
            merge(left, right, sorted);

            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                done.add(b);
            }
            todo += sorted.length;
        }

        merge *= 2; // Increase now
        /* Animate block repositions */
        syncPos(done, blks, row);
    }

    /**
     * Repositions each block in `source`
     * based on the position at an index
     * then puts each to `target`
     */
    public void syncPos(ArrayList<Block> source, Block[] target, ArrayList<Point> row) {
        /*
         * for (int i = 0; i < target.length; i++) {
         * var b = source.get(i);
         * b.setLocation(row.get(i));
         * target[i++] = b;
         * Animator.this.repaint();
         * }
         */

        // Timer code is like the above loop
        // Move each block every at a certain interval;
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (i >= target.length) {
                    this.cancel();
                    history.removeLast(); // Important: Delete tail when animation ends
                    animating = false;
                    return;
                }
                final Block b = source.get(i);
                b.setLocation(row.get(i));
                target[i++] = b;
                Animator.this.repaint();
            }
        }, 0, intervalMS);
    }

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

    @Override
    public void keyTyped(KeyEvent e) {
        // Pass
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Pass
    }
}