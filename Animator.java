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
    private final int baseWidth;
    private final JPanel panel;
    private final LinkedList<ArrayList<Point>> history = new LinkedList<>();
    final int todos;
    // `split` Represents the size of the subarrays made by MergeSort.
    int split; // Must do float division
    int merge = 1;
    Timer timer = new Timer();

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

    Animator(Block[] blocks, int baseWidth, JPanel panel) {
        this.baseWidth = baseWidth;
        this.blocks = blocks;
        this.panel = panel;
        todos = blocks.length;
        split = calcSplit(blocks);
    }

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
        int changeX = (int) ((b.getX() + (right ? 1 : -1) * baseWidth * _split));
        int changeY = (int) b.getY() + (down ? 1 : -1) * b.getHeight();
        b.setLocation(changeX, changeY);
        panel.repaint();
    }

    public void swapPlace(Block a, Block b, int bD) {
        b.setLocation(a.getLocation());
        move(b, false, false, bD);
    }

    /*
     * todo: Record split locations
     */
    public void goSplit(Block[] blks) {
        snapshot(blks); // Record
        TimerTask task = new TimerTask() {
            int done = 0;
            int index = 0;
            boolean right = false;

            @Override
            public void run() {
                if (done < todos) {
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
                    split/= 2;
                }
            }
        };

        // To move a block one at a time, run `task` at an interval.
        timer.scheduleAtFixedRate(task, 0, 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Activate by pressing right arrow key
        if (e.getKeyCode() != KeyEvent.VK_RIGHT)
            return;

        if (split > 0) { // Split step
            goSplit(blocks);
            return;
        }
        goMerge(blocks);
        System.out.println(history.size());
    }

    public void goMerge(Block[] blks) {
        final ArrayList<Point> row = history.peekLast();
        if (row == null) {
            System.out.println("done");
            return;
        }
        System.out.println(row);

        /* Move each block every 100ms */
        final TimerTask t = new TimerTask() {
            int i = 0;

            /*
             * Block[] left;
             * Block[] right;
             * Block[] target;
             */
            @Override
            public void run() {
                // End loop
                if (i >= blks.length) {
                    this.cancel();
                    history.removeLast(); // Delete the row

                    // Repeat animation
                    if (history.peekLast() == null) {
                        split = calcSplit(blks);
                    }
                    return;
                }

                // blks[i].setLocation(row.get(i++));
                int todo = 0;
                ArrayList<Block> done = new ArrayList<>(blks.length);
                System.out.println("merge size " + merge);
                while (todo < blks.length) {
                    /*
                     * for (int i = 0; i < merge; i++) {
                     * }
                     */
                    var offset = Math.min(todo + merge, blks.length);
                    var left = Arrays.copyOfRange(blocks, todo, offset);
                    var right = Arrays.copyOfRange(blocks, offset, Math.min(todo + merge * 2, blks.length));
                    var target = new Block[left.length + right.length];
                    animatedMerge(left, right, target);
                    for (int j = 0; j < target.length; j++) {
                        done.add(target[j]);
                    }
                    todo += target.length;
                }
                merge *= 2;
                syncPos(done, blks, row);
                /*
                 * for (int k = 0; k < done.size(); k++) {
                 * var b = done.get(k);
                 * b.setLocation(row.get(k));
                 * blocks[k] = b;
                 * }
                 * history.removeLast();
                 * Animator.this.repaint();
                 */
            }
        };

        timer.schedule(t, 100);
    }

    public void syncPos(ArrayList<Block> source, Block[] target, ArrayList<Point> row) {
        /*
         * for (int i = 0; i < target.length; i++) {
         * var b = source.get(i);
         * b.setLocation(row.get(i));
         * target[i++] = b;
         * Animator.this.repaint();
         * }
         */
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (i >= target.length) {
                    this.cancel();
                    history.removeLast();
                    return;
                }
                var b = source.get(i);
                b.setLocation(row.get(i));
                target[i++] = b;
                Animator.this.repaint();
            }
        }, 0, 100);
    }

    public void mergeSort(Block[] array) {
        if (array.length <= 1) {
            return; // base case
        }

        final int middle = calcSplit(array);
        // Split array into two
        Block[] left = Arrays.copyOfRange(array, 0, middle);
        Block[] right = Arrays.copyOfRange(array, middle, array.length);
        mergeSort(left);
        mergeSort(right);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                animatedMerge(left, right, array);
            }
        }, 100);
    }

    public void animatedMerge(Block[] left, Block[] right, Block[] target) {
        int i = 0, l = 0, r = 0; // indices
        // Check the conditions for merging
        while (l < left.length && r < right.length) {
            if (left[l].value < right[r].value) {
                target[i++] = left[l++];
            } else {
                target[i++] = right[r++];
            }
        }
        // Put the rest
        while (l < left.length) {
            target[i++] = left[l++];
        }
        while (r < right.length) {
            target[i++] = right[r++];
        }
    }

    public void merge(Block[] left, Block[] right, Block[] target) {
        int i = 0, l = 0, r = 0; // indices
        // Check the conditions for merging
        while (l < left.length && r < right.length) {
            final var _l = left[l];
            final var _r = right[r];
            if (_l == null || _r == null)
                break;

            if (_l.value < _r.value) {
                target[i++] = left[l++];
                move(_l, true, false, (int) (target.length / 0.75));
                System.out.println("keep " + _l.value);
            } else {
                target[i++] = right[r++];
                swapPlace(_l, _r, (int) (target.length / 0.75));
                System.out.println("swap " + _r.value);
            }
        }
        // Put the rest
        while (l < left.length) {
            final var _l = left[l++];
            if (_l == null)
                continue;
            move(_l, true, false, (int) (target.length / 0.75));
            System.out.println("add " + _l.value);
            target[i++] = _l;
        }
        while (r < right.length) {
            final var _r = right[r++];
            if (_r == null)
                continue;

            move(_r, false, false, (int) (target.length / 0.75));
            System.out.println("add " + _r.value);
            target[i++] = _r;
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