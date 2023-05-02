package Block;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import util.KPanel;

public class InputBlock extends Box {
    public final JTextField input;

    public InputBlock() {
        setOpaque(false);
        setBorder( new EmptyBorder(BlockLabel.DEFAULT_BORDER_SIZE, 8,BlockLabel.DEFAULT_BORDER_SIZE, 8));
        input = new JTextField(2);
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setFont(input.getFont().deriveFont(30f));
        input.setBorder(KPanel.squareBorder(0));
        add(input);

        input.addMouseListener(hoverer);
    }
}