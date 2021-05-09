package ui.windows;

import ui.I18N;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.*;

public abstract class EntityDialog<E> {

    private final JPanel panel = new JPanel();
    private final I18N i18n = new I18N(getClass());
    private int nextComponentRow = 0;

    EntityDialog() {
        panel.setLayout(new GridBagLayout());
    }

    protected static boolean areTextFieldsEmpty(List<JTextField> textFields) {
        for (JTextField textField : textFields) {
            if (textField.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    void add(String labelKey, JComponent component) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0.0;
        var label = new JLabel(i18n.getString(labelKey));
        label.setLabelFor(component);
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        panel.add(component, c);
    }

    abstract E getEntity();

    public abstract void setEntity(E entity);

    public abstract boolean isCorrect(StringBuilder msg);

    public Optional<E> showEdit(JComponent parentComponent) {
        return show(parentComponent, i18n.getString("title.edit"));
    }

    public Optional<E> showAdd(JComponent parentComponent) {
        return show(parentComponent, i18n.getString("title.add"));
    }

    private Optional<E> show(JComponent parentComponent, String title) {
        while (true) {
            int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                StringBuilder msg = new StringBuilder();
                if (!isCorrect(msg)) {
                    JOptionPane.showMessageDialog(null, msg.toString());
                    continue;
                }
                return Optional.of(getEntity());
            } else {
                return Optional.empty();
            }
        }
    }
}
