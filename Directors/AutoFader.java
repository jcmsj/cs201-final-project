package Directors;

import java.util.Arrays;
import java.util.Iterator;
import Block.Block;
import Block.Block.STATE;
import Block.Row;

public class AutoFader extends Fader {

    public AutoFader(int[] ints) {
        super(ints);
        anim.interval = 150;
    }
    
    @Override
    public void mergeSort() {
        super.mergeSort();
        keepAnimating();
    }

    public void keepAnimating() {
        nextRow(() -> keepAnimating());
    }
    @Override
    public void addListeners() {
        // PASS
    }

    private void nextRow(Runnable cb) {
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
                onEnd.run();
            }
        }, 0);
    }

    public void updateBlocks(Block b, Block[] previous) {
        b.show();
        //Find equivalent block from the previous row that isn't DIMMED
        for (Block p : previous) {
            if (p.state != STATE.DIMMED && b.value == p.value) {
                p.dim();
                break;
            }
        }
    }
}
