package Block;

/* A movable block with a number */
public class Block extends Box {
    public static float DEFAULT_FONT_SIZE = 30f;
    public int value;
    static final int side = 15; 
    public Block(int value) {
        this(value, DEFAULT_FONT_SIZE);
    }
    public Block(int value, float fontSize) {
        this.value = value;
        add(new BlockLabel(value, fontSize));
    }

    /**
     * For debugging
     */
    public String toString() {
        return "(v=" + value + ", x=" + getX() + ", y=" + getY() + ")";
    }

    /**
     * Creates a new `Block` with the same value.
     * 
     * @implNote does not keep other state (E.g. position) unlike with
     *           {@link Object#clone}
     */
    public Block dup() {
        return new Block(value);
    }

    /**
     * Similar to {@link java.util.Arrays#copyOfRange} but uses {@link Block#dup}
     * 
     * @see Block#dup
     */
    public static Block[] copyOfRange(Block[] original, int from, int to) throws IllegalArgumentException {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);

        newLength = Math.min(original.length - from, newLength);
        Block[] copy = new Block[newLength];

        for (int i = 0; i < newLength; i++) {
            copy[i] = original[i].dup();
        }

        return copy;
    }

    public static Block[] copy(Block[] blocks) {
        var copy = new Block[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            copy[i] = blocks[i].dup();
        }
        return copy;
    }

    public static Block[] asBlocks(int[] ints, float fontSize) {
        Block[] blocks = new Block[ints.length];
        for(int i =0 ; i< ints.length; i++) {
            blocks[i] = new Block(ints[i], fontSize);
        }
        return blocks;
    }
}