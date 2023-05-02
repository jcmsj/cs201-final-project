package util;

import javax.swing.JButton;
import javax.swing.border.Border;

public class KButton extends JButton {
    private final BorderChanger borderChanger;
    public KButton() {
        super();
        borderChanger = new BorderChanger(this);
        addFocusListener(borderChanger);
    
    }

    public void setFocusedBorder(Border f) {
        borderChanger.setFocusedBorder(f);
    }

    public void setUnFocusedBorder(Border u) {
        borderChanger.setUnfocusedBorder(u);
    }
}