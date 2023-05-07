package util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/* Assures assets are loaded when project is in a jar */
public class Loader {
    public static BufferedImage bufferedImage(String path) {
        try {
            return ImageIO.read(stream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream stream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static ImageIcon imageIcon(String path) {
        return new ImageIcon(bufferedImage(path));
    }
}