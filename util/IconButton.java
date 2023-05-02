package util;

import javax.swing.ImageIcon;

public class IconButton extends KButton {
    public IconButton(String path) {
        setIcon(new ImageIcon(getClass().getResource(path)));
    }
}