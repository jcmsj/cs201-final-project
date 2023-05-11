package Directors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import Block.Block;

/* 
 * Assists directors with common tasks
 */
public class Aqua {
    /**
     * Does the actual merge step with animation
     */
    public static void merge(Block[] left, Block[] right, Block[] target) {
        int i = 0, l = 0, r = 0; // indices
        // Check the conditions for merging
        // Important: Compare value field of blocks
        while (l < left.length && r < right.length) {
            target[i++] = left[l].value < right[r].value ? left[l++] : right[r++];
        }

        i = putRemaining(target, i, left, l);
        putRemaining(target, i, right, r);
    }

    /**
     * @param blocks provider of the blocks as well as where the sorted blocks would be placed
     * @param split
     */
    public static void mergeStep(Block[] blocks, int split) {
        System.out.println("Merge by " + split);
        ArrayList<Block> done = new ArrayList<>(blocks.length);
        while (done.size() < blocks.length) {
            // Split step in the actual merge sort
            final int progress = done.size();
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
        done.toArray(blocks);
    }
    /**
     * @return target index stop
     */
    public static int putRemaining(Block[] target, int targetI, Block[] source, int index) {
        while (index < source.length) {
            target[targetI++] = source[index++];
        }
        return targetI;
    }

    public static void syncPositions(Block[] sources, Block[] targets) {
        // Sync block positions
        for (int i = 0; i < targets.length; i++) {
            targets[i] = sources[i];
        }
    }

    public static LinkedList<Block> arrayToLinkedList(Block[] ts) {
        var ll = new LinkedList<Block>();
        for (var t : ts) {
            ll.add(t);
        }
        return ll;
    }
}
