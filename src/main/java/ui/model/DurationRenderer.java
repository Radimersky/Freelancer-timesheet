package ui.model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.Duration;

public final class DurationRenderer implements TableCellRenderer {
    private final DefaultTableCellRenderer tableCellRenderer = new DefaultTableCellRenderer();

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        var label = (JLabel) tableCellRenderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        updateLabel(label, (Duration) value);
        return label;
    }

    private void updateLabel(JLabel label, Duration duration) {
        label.setText(String.format("%d:%02d", duration.toHours(), duration.toMinutesPart()));
    }
}
