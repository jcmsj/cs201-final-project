package Directors;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import Block.Block;
import Block.Row;
import Style.Style;
import util.Animator;

public class Row2 extends Row {
    ArrayList<Block> done;
    int _split;
    public Row2(Block[] blocks, int split) {
        super(blocks, split);
        this._split = split;
        while(next()) {

        }
        done = new ArrayList<>(blocks.length);
    }

    Row2 next;
    public void derive(int split, Runnable onEnd) {
        System.out.println("Split by " + split);
        deriveIter(split/2, done, onEnd);
    }

    public static Row2 normalMerge(int split, Block[] blocks) {
        split /=2;
        System.out.println("Merge by " + split);
        ArrayList<Block> done2 = new ArrayList<>(blocks.length);
        while (done2.size() < blocks.length) {
            // Split step in the actual merge sort
            int progress = done2.size();
            final int offset = Math.min(progress + split, blocks.length);
            Block[] left = Arrays.copyOfRange(blocks, progress, offset);
            final int nextOffset = Math.min(offset + split, blocks.length);
            Block[] right = Arrays.copyOfRange(blocks, offset, nextOffset);
            Block[] sorted = new Block[left.length + right.length];
            Aqua.merge(left, right, sorted);
            // Add `sorted` to the previously sorted parts
            for (Block b : sorted) {
                // Important: duplicate block
                done2.add(b.dup());
            }
        }

        Aqua.syncPositions(done2, blocks);
        final Row2 r = new Row2(blocks, split*2);
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
            m2(left, right, target -> {
                done.addAll(target);
                deriveIter(split, done, onEnd);
            });
        } else {
            if (onEnd != null) {
                onEnd.run();
            }
        }
    }

    public void m2(Block[] _left, Block[] _right, Consumer<LinkedList<Block>> onEnd) {
        final LinkedList<Block> left = arrayToLinkedList(_left);
        final LinkedList<Block> right = arrayToLinkedList(_right);
        final LinkedList<Block> target = new LinkedList<Block>();

        compareOnce(left, right, target, () -> {
            putRemaining(left, target, () -> {
                putRemaining(right, target, () -> onEnd.accept(target));
            });
        });
    }

    static Animator anim = new Animator(200);
    static Border greenB = BorderFactory.createLineBorder(Color.green,3);
    static Border redB = BorderFactory.createLineBorder(Style.LIGHT_RED,3);

    public void compareOnce(
        LinkedList<Block> left, 
        LinkedList<Block> right, 
        LinkedList<Block> target,
            Runnable onEnd) {
        if (left.size() > 0 && right.size() > 0) {
            boolean isLeftLess = left.peek().value < right.peek().value;
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
            lower.setBorder(greenB);
            lower.repaint();
            anim.schedule(t -> {
                // Same w/ higher value but with red
                higher.setBorder(redB);
                higher.repaint();

                anim.schedule(tt -> {
                    // Show the equivalent value in the next row
                    // And dim the same value in the current row
                    if (next != null) {
                        show(lower);
                    }
                    //Repeat comparison with next set of values
                    anim.schedule(ttt -> {
                        anim.schedule(tttt -> {
                            compareOnce(left, right, target, onEnd);
                        });
                    });
                });
            });
        } else {
            onEnd.run();
        }
    }
    
    public void show(Block b) {
        for(var n: next.blocks) {
            if (b.value == n.value) {
                n.show();
                anim.schedule(t -> {
                    b.setBorder(Block.DEFAULT_BORDER);
                    b.dim();
                });
            }
        }
    }
    public static LinkedList<Block> arrayToLinkedList(Block[] ts) {
        var ll = new LinkedList<Block>();
        for (var t : ts) {
            ll.add(t);
        }
        return ll;
    }

    public static int putRemaining(Block[] target, int targetI, Block[] source, int index) {
        while (index < source.length) {
            target[targetI++] = source[index++];
        }

        return targetI;
    }

    public void putRemaining(LinkedList<Block> source, LinkedList<Block> target, Runnable after) {
        anim.every(t -> {
            if (source.isEmpty()) {
                t.cancel();
                after.run();
            } else {
                Block b = source.poll();
                b.setBorder(greenB);
                show(b);
                b.repaint();
                target.add(b);
            }
        }, 0);
    }
}
