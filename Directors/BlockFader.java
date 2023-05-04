package Directors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;
import Block.Block;
import Block.Row;
import Block.Block.STATE;

import java.awt.event.KeyEvent;

public class BlockFader extends Fader {

    public BlockFader(int[] ints) {
        super(ints);
    }

    public class TwoStack<E> extends Stack<E> {
        /* Atomically pushes two elements */
        public void pushTwo(E first, E second) {
            push(first);
            push(second);
        }
    }

    private Iterator<Block> forwardIter;
    private Row prev;
    private Row cur;
    private TwoStack<Block> actions = new TwoStack<>();
    private TwoStack<Block> removed = new TwoStack<>();

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this, t -> {
            // Use cached series of actions
            if (removed.size() > 0) {
                // Pop two from `removed` but repush two `actions`
                Block current = removed.pop();
                current.show();
                Block previous = removed.pop();
                previous.dim();
                actions.pushTwo(previous, current);
                return;
            }

            // Else, move using iterator
            if (forwardIter == null || !forwardIter.hasNext()) {
                if (rowI >= rows.size()) {
                    System.out.println("Animation end");
                    return;
                }

                // Proceed with next row
                prev = rows.get(rowI - 1);
                cur = rows.get(rowI);
                forwardIter = Arrays.stream(cur.blocks).iterator();
                rowI++;
            }
            Block current = forwardIter.next();
            current.show();

            // Find equivalent block from the previous row that isn't DIMMED
            for (Block previous : prev.blocks) {
                if (previous.state != STATE.DIMMED && current.value == previous.value) {
                    previous.dim();
                    actions.pushTwo(previous, current);
                    break;
                }
            }
            repaint();
        });
        // Activate by pressing left arrow key
        anim.onPress(KeyEvent.VK_LEFT, this, t -> {
            if (actions.size() <= 0) {
                System.out.println("Animation back to start!");
                return;
            }
            // Pop two from `actions` but repush to `removed`
            Block current = actions.pop();
            current.hide();
            Block previous = actions.pop();
            previous.show();
            removed.pushTwo(previous, current);
        });
    }
}