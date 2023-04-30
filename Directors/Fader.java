package Directors;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;

import Block.Block;
import Block.Row;

public class Fader extends RowDirector {
    public Fader(int[] ints, float fontSize) {
        super(ints, fontSize);
        anim.interval = 100;
        mergeSort();
    }

    public Fader(int[] ints) {
        super(ints);
        anim.interval = 100;
        mergeSort();
    }

    public Fader(Block[] blocks) {
        super(blocks);
        anim.interval = 100;
        mergeSort();
    }

    @Override
    public void mergeSort() {
        while (split < blocks.length) {
            super.mergeSort();
        }
    }

    int rowI = 1;

    @Override
    public void addListeners() {
        // Activate by pressing right arrow key
        anim.onPress(KeyEvent.VK_RIGHT, this,
                t -> {
                    System.out.println("rowI="+rowI);
                    if (rowI >= rows.size()) {
                        System.out.println("Animation end");
                        return;
                    }

                    Row prev = rows.get(rowI - 1);
                    Row cur = rows.get(rowI);
                    var iter = Arrays.stream(cur.blocks).iterator();
                    anim.every(tt -> {
                        if (iter.hasNext()) {
                            Block b = iter.next();
                            b.show();
                            for (Block p : prev.blocks) {
                                if (p.isShown && b.value == p.value) {
                                    p.hide();
                                    break;
                                }
                            }
                        } else {
                            tt.cancel();
                        }
                    }, 0);
                    rowI++;
                });

        // Activate by pressing left arrow key
        anim.onPress(KeyEvent.VK_LEFT, this,
        t -> onPrev());
        
    }

    public void onPrev() {
        System.out.println("rowI="+rowI);
        if (rowI <= 1) {
            rowI = 1;
            System.out.println("Animation back to start");
            return;
        }

        Row prev = rows.get(rowI-2);
        Row cur = rows.get(rowI-1);
        rowI--;
        Iterator<Block> iter = Arrays.stream(cur.blocks).iterator();
        anim.every(tt -> {
            if (iter.hasNext()) {
                Block b = iter.next();
                b.hide();
                for (Block p : prev.blocks) {
                    if (!p.isShown && b.value == p.value) {
                        p.show();
                        break;
                    }
                }
            } else {
                tt.cancel();
            }
        }, 0);
    }

    @Override
    public void animateRow(Row r, Runnable onEnd) {
        // Set blocks as invisible
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
}