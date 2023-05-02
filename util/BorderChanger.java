package util;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;
import javax.swing.border.Border;

public class BorderChanger implements FocusListener {
    private Border focusedBorder;
    private Border unfocusedBorder;
    private JComponent target; 
    
    public BorderChanger(JComponent target) {
        this.target = target;
        unfocusedBorder = target.getBorder();
    }
    @Override
    public void focusGained(FocusEvent e) {
        if (focusedBorder != null)
            target.setBorder(focusedBorder);
    }
    @Override
    public void focusLost(FocusEvent e) {
        target.setBorder(unfocusedBorder);
    }

    public void setFocusedBorder(Border f) {
        focusedBorder = f;
    }

    public void setUnfocusedBorder(Border u) {
        unfocusedBorder = u;
    }
}
