import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Animator extends JPanel implements KeyListener {
    private final Block[] blocks;
    private final int moveDistanceX;
    private final Stack<ArrayList<Point>> history = new Stack<>();
    private final Timer timer = new Timer();
    private int intervalMS = 50; // in ms
    private boolean animating = false;
    private int count = 0;

    public static ArrayList<Point> blocksToLocation(Block[] blocks) {
        return Arrays.stream(blocks)
                .map(b -> b.getLocation())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Records the current location of all blocks
     */
    public boolean snapshot(Block[] blocks) {
        var res = history.add(blocksToLocation(blocks));
        ArrayList<Block> r = new ArrayList<>();
        for (var b : blocks) {
            r.add(b);
        }
        processed.add(r);
        return res;
    }

    Animator(Block[] blocks, int moveDistanceX, int intervalMS) {
        setBorder(new EmptyBorder(30, 30, 30, 30));
        setFocusable(true);
        setVisible(true);
        addKeyListener(this);
        for (var b : blocks) {
            add(b);
        }
        this.moveDistanceX = moveDistanceX;
        this.blocks = blocks;
        this.intervalMS = intervalMS;
        queue.push(() -> goSplit(blocks, null));
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
        return (int) Math.round(n / 2.0);
    }

    public void move(Block b, boolean right) {
        move(b, right, true, 1);
    }

    public void move(Block b, boolean right, boolean down, int xModifier) {
        int changeX = (int) ((b.getX() + (right ? 1 : -1) * moveDistanceX * xModifier));
        int changeY = (int) b.getY() + (down ? 1 : -1) * b.getHeight();
        b.setLocation(changeX, changeY);
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Activate by pressing right arrow key
        if (e.getKeyCode() != KeyEvent.VK_RIGHT) {
            return;
        }
        if (animating) {
            System.out.println("Already animating!");
            return;
        }

        animating = true;

        if (queue.peek() == null) {
            System.out.println("merging...");
            goMerge();
            return;
        }
        Runnable r = queue.poll();
        while (r != null) {
            r.run();
            r = queue.poll();
        }

    }
    public LinkedList<Runnable> mergeQueue = new LinkedList<>();

    public void animMove(Block[] side, boolean right, Runnable whenDone) {
        ArrayList<Point> r = new ArrayList<>();
        count += side.length;
        snapshot(side);
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (i >= side.length) {
                    this.cancel();
                    return;
                }
                // System.out.println(right + " " + side[i]);
                r.add(side[i].getLocation());
                move(side[i++], right, true, side.length);
            }

            @Override
            public boolean cancel() {
                if (whenDone != null)
                    whenDone.run();
                return super.cancel();
            }
        }, 0, intervalMS);
    }

    public Stack<ArrayList<Block>> processed = new Stack<>();

    public LinkedList<Runnable> queue = new LinkedList<>();

    public void goSplit(Block[] blks, Runnable whenDone) {
        if (blks.length == 1) {
            animating = false;
            queue.pollLast();
            return;
        }
        // Split step in the actual merge sort
        int mid = calcSplit(blks.length);
        System.out.println("split by " + mid);
        Block[] left = Arrays.copyOfRange(blks, 0, mid);
        Block[] right = Arrays.copyOfRange(blks, mid, blks.length);
        mergeQueue.add(() -> {
            Block[] sorted = new Block[left.length + right.length];
            merge(left, right, sorted);
        });
        /*
         * ArrayList<Block> p = new ArrayList<>(blks.length);
         * for (var b : blks) {
         * p.add(b);
         * }
         * 
         * processed.add(p);
         */
        // Animate left blocks
        animMove(left, false, () -> {
            // Then animate right blocks
            animMove(right, true, () -> {
                // Prepare next split
                queue.add(
                        () -> goSplit(left,
                                () -> goSplit(right, null)));

                animating = false;
                if (whenDone != null) {
                    whenDone.run();
                }
            });
        });

    }

    public void goMerge() {
        int todo = count % blocks.length;
        ArrayList<Block> done = new ArrayList<>(blocks.length);
        ArrayList<Point> row = new ArrayList<>();
        System.out.println("Moves: " + count);
        while (todo > 0) {
            try {
                ArrayList<Point> part = history.pop();
                ArrayList<Block> p = processed.pop();
                todo -= part.size();
                System.out.println("Points= " + part.size() + " Blocks=" + p.size());
                done.addAll(p);
                row.addAll(part);
            } catch (EmptyStackException e) {
                System.out.println("Looping...");
                // RESET
                history.clear();
                processed.clear();
                mergeQueue.clear();
                count = 0;
                animating = false;
                goSplit(blocks, null);
                return;
            }
        }
        Runnable r = mergeQueue.pollFirst();
        if (r != null) {
            r.run();
        }

        if (done.size() > 0) {
            syncPos(done, blocks, row);
        }
        animating = false;
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
            public boolean cancel() {
                // history.pop(); // Important: Delete tail when animation ends
                animating = false;
                return super.cancel();
            }

            @Override
            public void run() {
                if (i >= source.size()) {
                    this.cancel();
                    return;
                }
                final Block b = source.get(i);
                if (b == null) {
                    // uh oh
                    return;
                }
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