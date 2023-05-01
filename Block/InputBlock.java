package Block;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class InputBlock extends Box {
    public JTextField input;

    public InputBlock() {
        input = new JTextField(2);
        input.setFont(input.getFont().deriveFont(32f));
        input.setBorder(new EmptyBorder(5, 1, 5, 1));
        add(input);
    }
}
