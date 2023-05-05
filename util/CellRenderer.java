package util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import Style.Style;

public class CellRenderer extends JPanel implements ListCellRenderer<ANIM_MODE> {
    JLabel label;
    Border border;
    Border focusedBorder;
    CompoundBorder selectedBorder;
    private CompoundBorder focusedAndSelectedBorder;
    public final static Color CELL_COLOR = Color.decode("#E2DCDB");

    public CellRenderer() {
        label = new JLabel();
        label.setForeground(CELL_COLOR);
        label.setFont(label.getFont().deriveFont(25f));
        border = KPanel.paddedBorder(Style.wine, 2, 5);
        focusedBorder = KPanel.paddedBorder(Style.yellowish.darker(), 2,5);
        focusedAndSelectedBorder = KPanel.paddedBorder(Style.yellowish, 2,5);
        add(label);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ANIM_MODE> list, ANIM_MODE value, int index,
            boolean isSelected, boolean cellHasFocus) {
        if (cellHasFocus) {
            setBorder(focusedBorder);
        } else {
            setBorder(border);
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            if (cellHasFocus) {
                setBorder(focusedAndSelectedBorder);
            }
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        label.setText(value.toString());
        return this;
    }

}
