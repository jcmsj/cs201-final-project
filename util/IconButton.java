package util;

public class IconButton extends KButton {
    public IconButton(String path) {
        setIcon(Loader.imageIcon(path));
    }
}