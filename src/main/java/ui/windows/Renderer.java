package ui.windows;


import javax.swing.*;
import java.awt.*;

final class Renderer<T> implements ListCellRenderer<T> {

    private final DefaultListCellRenderer listCellRenderer = new DefaultListCellRenderer();


    @Override
    public Component getListCellRendererComponent(
            JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus) {

        var label = (JLabel) listCellRenderer.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        updateLabel(label, value);
        return label;
    }

    private void updateLabel(JLabel label, T value) {
        label.setText(value.toString());
    }
}
