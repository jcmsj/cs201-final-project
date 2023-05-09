package Directors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import Block.Block;
import Block.Row;
import Block.Block.STATE;
import util.Animator;

public class Row2 extends Row {
    ArrayList<Block> done;
    int _split;
    Row2 next;

    public Row2(Block[] blocks, int split) {
        super(blocks, split);
        this._split = split;
        /* Immediately paint blocks */
        revealAll();
        done = new ArrayList<>(blocks.length);
    }

    public void derive(int split, Runnable onEnd) {
        System.out.println("Split by " + split);
        deriveIter(split / 2, done, onEnd);
    }

    public static Row2 normalMerge(int split, Block[] blocks) {
        split /= 2;
        System.out.println("Merge by " + split);
        Aqua.mergeStep(blocks, split);
        final Row2 r = new Row2(blocks, split * 2);
        return r;
    }

    public void deriveIter(int split, ArrayList<Block> done, Runnable onEnd) {
        if (done.size() < blocks.length) {
            // Split step in the actual merge sort
            int progress = done.size();
            final int offset = Math.min(progress + split, blocks.length);
            final Block[] left = Arrays.copyOfRange(blocks, progress, offset);
            final int nextOffset = Math.min(offset + split, blocks.length);
            final Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            mergeStep(left, right, target -> {
                done.addAll(target);
                anim.schedule(() -> deriveIter(split, done, onEnd));
            });
        } else if (onEnd != null) {
            onEnd.run();
        }
    }

    /**
     * A modified version of Aqua.mergeStep for the new animation.
     */
    public void mergeStep(Block[] _left, Block[] _right, Consumer<LinkedList<Block>> onEnd) {
        final LinkedList<Block> left = Aqua.arrayToLinkedList(_left);
        final LinkedList<Block> right = Aqua.arrayToLinkedList(_right);
        final LinkedList<Block> target = new LinkedList<Block>();

        compareOnce(left, right, target, () -> {
            takeAll(left, target, () -> {
                takeAll(right, target, () -> onEnd.accept(target));
            });
        });
    }

    static Animator anim = new Animator(200);

    public void compareOnce(
            LinkedList<Block> left,
            LinkedList<Block> right,
            LinkedList<Block> target,
            Runnable onEnd) {
        if (left.size() <= 0 || right.size() <= 0) {
            onEnd.run();
            return;
        }
        boolean isLeftLess = left.peek().value <= right.peek().value;
        Block lower, higher;
        if (isLeftLess) {
            lower = left.poll();
        } else {
            lower = right.poll();
        }
        if (isLeftLess) {
            higher = right.peek();
        } else {
            higher = left.peek();
        }
        target.add(lower);

        // Dim copy
        Fader.updateBlocks(lower, blocks);
        // Highlight lower value with green border
        lower.useGreenBorder();
        anim.schedule(() -> {
            // Same w/ higher value but with red
            higher.useRedBorder();

            anim.schedule(() -> {
                // Show the equivalent value in the next row
                // And dim the same value in the current row
                // Repeat comparison with next set of values
                Runnable cb = () -> compareOnce(left, right, target, onEnd);
                if (next == null) {
                    anim.schedule(cb);
                } else {
                    show(lower, cb);
                }
            });
        });
    }

    public void dimAndDefault(Block b, Runnable onEnd) {
        anim.schedule(t -> {
            b.useDefaultBorder();
            b.dim();
            if (onEnd != null) {
                anim.schedule(onEnd);
            }
        });
    }

    public void show(Block b, Runnable onEnd) {
        for (var n : next.blocks) {
            if (n.state != STATE.SHOWN && b.value == n.value) {
                n.showMe();
                dimAndDefault(b, onEnd);
                break;
            }
        }
    }

    public void takeAll(LinkedList<Block> source, LinkedList<Block> target, Runnable after) {
        anim.every(t -> {
            if (!putOne(source.poll(), target)) {
                t.cancel();
                after.run();
            }
        }, 0);
    }

    /**
     * @return whether an element was taken from the source
     */
    public boolean putOne(Block b, LinkedList<Block> target) {
        if (b == null) {
            return false;
        }
        b.useGreenBorder();
        show(b, () -> dimAndDefault(b, null));
        target.add(b);
        return true;
    }
}