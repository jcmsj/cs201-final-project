package Block;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class BlockLabel extends JLabel {
    public static final int BORDER_MOD_L = 15;
    public static final int BORDER_MOD_W = 15;

    public BlockLabel(int value, float fontSize) {
        super("" + value);
        setFont(getFont().deriveFont(fontSize));
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(calcDynamicBorder(fontSize));
    }

    public static Border calcDynamicBorder(float fontSize) {
        /* Sets an arbitrary border based on the fontSize */
        int L = Math.max((int) (fontSize / BORDER_MOD_L) + BORDER_MOD_L, BORDER_MOD_L);
        int W = Math.max((int) (fontSize / BORDER_MOD_W) + BORDER_MOD_W, BORDER_MOD_W);
        return new EmptyBorder(L, W, L, W);
    }
}