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

    @Override
    public void nextRow(Runnable cb) {
        if (animating)
            return;
            
        if (split >= blocks.length) {
            System.out.println("DONE SORTING!");
            return;
        }
        lock();
        add(r2);
        split *= 2;
        var next = Row2.normalMerge(split, blocks);
        for (var b : next.blocks) {
            b.hideMe();
        }
        add(next);
        r2.next = next;
        r2.derive(split).thenRun(() -> {
            unlock();
            if (cb != null)
                cb.run();
        });
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