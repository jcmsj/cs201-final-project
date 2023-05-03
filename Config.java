import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import Style.Style;

public class Config extends JPanel {
    ModeButton modeManual;
    ModeButton modeSemi;
    ModeButton modeAuto;
    private JPanel centerPanel;
    public JButton activator;
    final static int MIN_WIDTH = 150;

    public static class ModeButton extends JRadioButton {
        public ModeButton(String text, ANIM_MODE m) {
            super(text);
            setActionCommand(m.toString());
            setFont(getFont().deriveFont(20f));
            setBackground(Style.wine2);
        }
    }

    public Config(int width, int height, Consumer<ANIM_MODE> onModeChange) {
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


        modeAuto = new ModeButton("Auto", ANIM_MODE.AUTO);
        modeAuto.setEnabled(false); // TODO: Auto mode
        modeSemi = new ModeButton("Semi", ANIM_MODE.SEMI);
        modeSemi.setSelected(true);
        modeManual = new ModeButton("Manual", ANIM_MODE.MANUAL);
        centerPanel = new JPanel();
        centerPanel.setLayout( new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        ItemListener modeChanger = ev -> {
            if (ev.getStateChange() == ItemEvent.SELECTED) {
                JRadioButton b = (JRadioButton) ev.getItem();
                onModeChange.accept(ANIM_MODE.valueOf(b.getActionCommand()));
            }
        };
        modeAuto.addItemListener(modeChanger);
        modeSemi.addItemListener(modeChanger);
        modeManual.addItemListener(modeChanger);
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(modeAuto);
        modeGroup.add(modeSemi);
        modeGroup.add(modeManual);
        
        JLabel modeTitle = new JLabel("Animation mode: ");
        modeTitle.setFont(modeTitle.getFont().deriveFont(20f));
        centerPanel.add(modeTitle);
        centerPanel.add(modeAuto);
        centerPanel.add(modeSemi);
        centerPanel.add(modeManual);
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
