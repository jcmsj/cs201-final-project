package util;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {
    public IconButton(String path) {
        setIcon(new ImageIcon(getClass().getResource(path)));
    }
}