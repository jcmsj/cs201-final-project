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
    /* DARK_RED is the same color with the block image */
    public final static Color DARK_RED = Color.decode("#4d1212");
    public final static Color reddish2 = Color.decode("#6e1818");

    public static Font alagard = null;
    public static Font material = null;

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
        alagard = registerFont("./assets/alagard.ttf");

        // JPanel
        UIManager.put("Panel.font", alagard);
        UIManager.put("Panel.background", Style.wine);

        // JLabel
        UIManager.put("Label.font", alagard);
        UIManager.put("Label.foreground", Style.yellowish);

        // JFrame
        UIManager.put("Frame.background", Style.yellowish);

        // JButton
        UIManager.put("Button.foreground", Style.yellowish);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Style.yellowish));
        UIManager.put("Button.background", Style.wine);
        UIManager.put("Button.focus", Style.yellowish);
        UIManager.put("Button.select", Style.yellowish);

        // JTextField
        UIManager.put("TextField.background", Style.wine);
        UIManager.put("TextField.font", alagard);
        UIManager.put("TextField.foreground", Style.yellowish);
        UIManager.put("TextField.caretForeground", Style.yellowish);
    }
}