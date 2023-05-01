package Block;
import javax.swing.JLabel;
import util.KPanel;

public class BlockLabel extends JLabel {
    static final int DEFAULT_BORDER_SIZE = 25;
    public BlockLabel(int value, float fontSize, int borderSize) {
        super("" + value);
        setFont(getFont().deriveFont(fontSize));
        setHorizontalAlignment(JLabel.CENTER);
        setBorder(KPanel.squareBorder(borderSize));
    }

    public BlockLabel(int value, float fontSize) {
        this(value, fontSize, DEFAULT_BORDER_SIZE);
    }
}