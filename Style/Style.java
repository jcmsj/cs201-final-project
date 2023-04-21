package Style;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

public class Style {
    /* Put colors as static members here */
    public final static Color reddish =  Color.decode("#FED37F");

    /* Change component values here */
    public static void init(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(".\\assets\\alagard.ttf"));
            ge.registerFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return;
        }
        UIManager.put("Panel.font", font);
    }
}