package Directors;

import java.awt.event.KeyEvent;

public class RowFader extends Fader {

    public RowFader(int[] ints) {
        super(ints);
    }

    private Row2 r2;

    @Override
    public void mergeSort() {
        split = 1;
        r2 = new Row2(blocks, split);
        add(r2);
    }

    private boolean animating = false;
    public void unlock() {
        animating = false;
    }

    @Override
    public void nextRow(Runnable cb) {
        if (animating)
            return;
            
        if (split >= blocks.length) {
            System.out.println("DONE SORTING!");
            return;
        }
        animating = true;
        add(r2);
        split *= 2;
        var next = Row2.normalMerge(split, blocks);
        for (var b : next.blocks) {
            b.hide();
        }
        add(next);
        r2.next = next;
        r2.derive(split, this::unlock);
        r2 = next;
        revalidate();
    }

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this,
                t -> nextRow(null));
    }
}