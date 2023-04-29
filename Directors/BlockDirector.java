package Directors;
import Block.Block;
import Block.Row;
import util.On;
import java.awt.event.KeyEvent;


/**
 * Controls a row to animate in/out a single block in one activation.
 */
public class BlockDirector extends RowDirector {
    private Row row;
    private Runnable onEnd;

    public BlockDirector(int[] ints) {
        super(ints);
    }
    
    public BlockDirector(Block[] blocks) {
        super(blocks);
    }

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        On.press(KeyEvent.VK_RIGHT, this, ev -> {
            if (row == null) {
                mergeSort();
            } else {
                anim.schedule(t -> {
                    if (row.next()) {
                        System.out.println("Next block");
                    } else {
                        System.out.println("Row done");
                        row = null;
                        if (onEnd != null) {
                            onEnd.run();
                            onEnd = null;
                        }
                    }
                });
            }
        });

        // Activate by pressing left arrow key
        // TODO: Reverse animation
        // TODO: Fix, having to activate the key twice to remove row
        On.press(KeyEvent.VK_LEFT, this, ev -> {
            Row r = rows.peekLast();
            if (r == null) {
                System.out.println("Empty rows");
                return;
            }
            anim.schedule(t -> {
                if (r.undo()) {
                    System.out.println("Prev block");
                } else {
                    if (row instanceof Row && row.equals(r)) {
                        row = null;
                    }
                    syncPositions(r.blocks);
                    remove(r);
                    split /=2;
                    if (onEnd != null) {
                        onEnd.run();
                        onEnd = null;
                    }
                }
            });
        });
    }

    @Override
    public void animateRow(Row r, Runnable onEnd) {
        this.row = r;
        this.onEnd = onEnd;
        r.next();
    }
}
