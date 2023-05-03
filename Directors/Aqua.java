package Directors;

import java.util.ArrayList;
import java.util.LinkedList;
import Block.Block;

/* 
 * Assists directors with common tasks
 */
public class Aqua {
    /**
     * Makes a true clone of the indices
     */
    public static LinkedList<Integer> dup(LinkedList<Integer> indices) {
        LinkedList<Integer> copy = new LinkedList<>();
        for (var index : indices) {
            copy.add(index);
        }
        return copy;
    }

    public static boolean exactlyOne(LinkedList<Integer> ints, int n) {
        int count = 0;
        for (Integer x : ints) {
            count += x.intValue() == n ? 1 : 0;
        }
        return count == 1;
    }
    public static boolean allTwos(LinkedList<Integer> ints) {
        return ints.stream().allMatch(t -> t == 2);
    }
    /**
     * Does the actual merge step in merge sort
     */
    public static void merge(Block[] left, Block[] right, Block[] target) {
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

    public static void syncPositions(ArrayList<Block> sources, Block[] targets) {
        // Sync block positions
        for (int i = 0; i < targets.length; i++) {
            targets[i] = sources.get(i);
        }
    }
    public static void syncPositions(Block[] sources, Block[] targets) {
        // Sync block positions
        for (int i = 0; i < targets.length; i++) {
            targets[i] = sources[i];
        }
    }
}
