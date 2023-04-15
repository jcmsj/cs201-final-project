package Final;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/* A movable block with a number */
public class Block extends JPanel {
    public final int  value;
    static int side = 15;
    public Block(Point pos, int value) {
        this.setFont(new Font("Arial", Font.BOLD, 20));
        this.setLocation(pos);
        this.value = value;
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        JLabel label = new JLabel("" + value);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(side, side, side, side));
        panel.add(label);
        this.add(panel);
    }
}
