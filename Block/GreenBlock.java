package Block;

import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import Style.Style;
import util.IconButton;

public class GreenBlock extends IconButton {
    public GreenBlock() {
        super("../assets/plus.png");
        setBackground(Style.DARK_GREEN);
        var inner = new EmptyBorder(16, 8, 16, 8);
        setBorder(inner);
        borderChanger.setUnfocusedBorder(getBorder());
        var outer = BorderFactory.createLineBorder(Style.yellowish, 1);
        var focused = BorderFactory.createCompoundBorder(outer, inner);
        setFocusedBorder(focused);
    }
}
