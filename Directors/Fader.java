package Directors;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import Block.Block;
import Block.Row;
import Block.Block.STATE;

public class Fader extends RowDirector {
    private void init() {
        anim.interval = 100;
        mergeSort();
    }

    public Fader(int[] ints, float fontSize) {
        super(ints, fontSize);
        init();
    }

    public Fader(int[] ints) {
        super(ints);
        init();
    }

    public Fader(Block[] blocks) {
        super(blocks);
        init();
    }

    @Override
    public void mergeSort() {
        while (split < blocks.length) {
            super.mergeSort();
        }
    }

    protected int rowI = 1;

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this, t -> nextRow(null));

        // Activate by pressing left arrow key
        anim.onPress(KeyEvent.VK_LEFT, this, t -> {
            if (rowI <= 1) {
                rowI = 1;
                System.out.println("Animation back to start");
                return;
            }

            rowI--;
            Row prev = rows.get(rowI - 1);
            Row cur = rows.get(rowI);
            Iterator<Block> iter = Arrays.stream(cur.blocks).iterator();
            anim.every(tt -> {
                if (iter.hasNext()) {
                    Block b = iter.next();
                    b.hide();
                    // Find equivalent block from previous row that isn't shown
                    for (Block p : prev.blocks) {
                        if (p.state != STATE.SHOWN && b.value == p.value) {
                            p.show();
                            break;
                        }
                    }
                } else {
                    tt.cancel();
                }
            }, 0);
        });
    }

    @Override
    public void animateRow(Row r, Runnable onEnd) {
        // If not the 1st row, set blocks as invisible
        if (split > 1) {
            for (var b : r.blocks) {
                b.hide();
                r.next();
            }
            return;
        }
        // Instantly show 1st row
        r.revealAll();
    }

    public void nextRow(Runnable cb) {
        if (rowI >= rows.size()) {
            System.out.println("Animation end");
            return;
        }
        Row prev = rows.get(rowI - 1);
        Row cur = rows.get(rowI);
        var iter = Arrays.stream(cur.blocks).iterator();
        handleRow(iter, prev.blocks, cb);
        rowI++;
    }

    public void handleRow(Iterator<Block> iter, Block[] previous, Runnable onEnd) {
        anim.every(tt -> {
            if (iter.hasNext()) {
                updateBlocks(iter.next(), previous);
            } else {
                tt.cancel();
                if (onEnd != null)
                    onEnd.run();
            }
        }, 0);
    }

    public static void updateBlocks(Block b, Block[] previous) {
        // Find equivalent block from the previous row that isn't DIMMED
        for (Block p : previous) {
            if (p.state != STATE.DIMMED && b.value == p.value) {
                p.dim();
                break;
            }
        }
        b.show();
    }
}