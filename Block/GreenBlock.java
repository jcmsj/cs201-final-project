package Block;

import Style.Style;
import util.IconButton;
import util.KPanel;

public class GreenBlock extends IconButton {
    public GreenBlock() {
        super("assets/plus.png");
        setBackground(Style.DARK_GREEN);
        var focused = KPanel.paddedBorder(Style.yellowish, 1, 16, 8, 16, 8);
        setBorder(focused.getInsideBorder());
        borderChanger.setUnfocusedBorder(getBorder());
        setFocusedBorder(focused);
    }
}