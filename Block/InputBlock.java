package Block;

import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import util.KPanel;

public class InputBlock extends Box {
    public final JTextField input;

    public InputBlock() {
        setOpaque(false);
        setBorder(new EmptyBorder(BlockLabel.DEFAULT_BORDER_SIZE, 8, BlockLabel.DEFAULT_BORDER_SIZE, 8));
        input = new JTextField(2);
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setFont(input.getFont().deriveFont(30f));
        input.setBorder(KPanel.squareBorder(0));
        add(input);
        input.addMouseListener(hoverer);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    input.requestFocusInWindow();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    input.requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void edit() {
        EventQueue.invokeLater(() -> {
            input.requestFocusInWindow();
        });
    }
}