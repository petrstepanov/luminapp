package ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class SelectedListCellRenderer extends DefaultListCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (isSelected) {
            c.setBackground(Color.decode("#b6f1ec"));
            c.setForeground(Color.BLACK);
        }
        return c;
    }

}
