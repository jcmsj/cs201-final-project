import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Style.Style;

/* A movable block with a number */
public class Block extends JPanel {
    public final int  value;
    static int side = 15;
    static Image image = new ImageIcon("./assets/block.png").getImage(); 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        {
            g.drawImage(image,0,0,getWidth(),getHeight(),this);
        }
    }
    public Block(int value) {
        this.value = value;
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        JLabel label = new JLabel("" + value);
        label.setFont(new Font("Alagard", Font.PLAIN, 40));
        label.setForeground(Style.reddish);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(side, side, side, side));
        panel.add(label);
        add(panel);
    }

    /**
     * For debugging
     */
    public String toString() {
        return "" + this.value + " " + this.getLocation();
    }
}
