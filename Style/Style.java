package Style;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.UIManager;

public class Style {
    /* Put colors as static members here */
    public final static Color yellowish = Color.decode("#FED37F");
    public final static Color wine = Color.decode("#722F37");
    public final static Color wine2 = Color.decode("#983e49");
    /* DARK_RED is the same color with the block image */
    public final static Color DARK_RED = Color.decode("#4d1212");
    public final static Color reddish2 = Color.decode("#6e1818");
    public final static Color LIGHT_RED = Color.decode("#d70107");
    public static Font alagard = null;

    public static Font registerFont(String path) {
        try {
            var font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Change component values here */
    public static void init() {
        alagard = registerFont("./assets/alagard.ttf").deriveFont(13f);
        // JPanel
        UIManager.put("Panel.font", alagard);
        UIManager.put("Panel.background", wine);

        // JLabel
        UIManager.put("Label.font", alagard);
        UIManager.put("Label.foreground", yellowish);

        // JFrame
        UIManager.put("Frame.background", yellowish);

        // JButton
        UIManager.put("Button.foreground", yellowish);
        UIManager.put("Button.border", BorderFactory.createLineBorder(yellowish));
        UIManager.put("Button.background", wine);
        UIManager.put("Button.focus", yellowish);
        UIManager.put("Button.select", yellowish);
        UIManager.put("Button.font", alagard);

        // JTextField
        UIManager.put("TextField.background", DARK_RED);
        UIManager.put("TextField.font", alagard);
        UIManager.put("TextField.foreground", yellowish);
        UIManager.put("TextField.caretForeground", yellowish);

        // Tooltip
        UIManager.put("ToolTip.foreground", yellowish);
        UIManager.put("ToolTip.background", wine2);
        UIManager.put("ToolTip.font", alagard.deriveFont(20f));
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(wine, 1));
    }
}