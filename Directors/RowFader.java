package Directors;

import java.awt.event.KeyEvent;

public class RowFader extends Fader {

    public RowFader(int[] ints) {
        super(ints);
    }

    Row2 r2;

    @Override
    public void mergeSort() {
        if (split >= blocks.length) {
            System.out.println("Sorted!");
            return;
        }
        split = 1;
        r2 = new Row2(blocks, split);
        add(r2);
        // mergeStep();
    }

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this,
                t -> {
                    if (split >= blocks.length) {
                        System.out.println("DONE SORTING!");
                        return;
                    }
                    add(r2);
                    split *= 2;
                    var next = Row2.normalMerge(split, blocks);
                    for (var b : next.blocks) {
                        b.hide();
                        b.repaint();
                    }
                    add(next);
                    r2.next = next;
                    r2.derive(split);
                    r2 = next;
                    revalidate();
                });

        // Activate by pressing left arrow key
        /*
         * anim.onPress(KeyEvent.VK_LEFT, this,
         * t -> undoLastRow());
         */
    }
}
