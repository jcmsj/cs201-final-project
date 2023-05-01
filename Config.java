import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import Style.Style;

public class Config extends JPanel {
    private JButton button2, button3, button4, button5;
    private JPanel centerPanel;
    public JButton activator;
    final static int MIN_WIDTH = 150;

    public Config(int width, int height) {
        // Create the sidebar panel and add it to the frame
        super(new GridBagLayout());
        setBackground(Style.wine2);
        // Limit
        width = Math.max(MIN_WIDTH, width);
        setBounds(0, 0, width, height); // x, y, width, height
        setBorder(BorderFactory.createEmptyBorder(100, 20, 20, 20));

        ImageIcon icon = new ImageIcon("./assets/gear.png");
        {
            Image image = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
        }

        // Create the button and add it to the frame
        // Toggling this panel should be handled by parent
        activator = new JButton(icon);
        activator.setToolTipText("Toggle settings panel");
        activator.setBounds(2, 2, 40, 40); // x, y, width, height
        activator.setBorderPainted(false);
        activator.setFocusable(true);
        Dimension d = new Dimension(90, 40);
        button2 = new JButton();
        button2.setPreferredSize(d);
        button2.setFocusable(false);
        button2.setText("Fonts ");
        button3 = new JButton();
        button3.setPreferredSize(d);
        button3.setFocusable(false);
        button3.setText("Background");
        button4 = new JButton();
        button4.setPreferredSize(d);
        button4.setText("item 3 ");
        button4.setFocusable(false);
        button5 = new JButton();
        button5.setPreferredSize(d);
        button5.setText("item 4 ");
        button5.setFocusable(false);

        centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setSize(200, 200);
        centerPanel.add(button2);
        centerPanel.add(button3);
        centerPanel.add(button4);
        centerPanel.add(button5);
        add(centerPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(centerPanel, gbc);
    }
}
