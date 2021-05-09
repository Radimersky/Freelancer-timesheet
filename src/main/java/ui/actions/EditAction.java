package ui.actions;

import ui.I18N;
import ui.Tab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public final class EditAction extends AbstractAction {

    private static final ui.I18N I18N = new I18N(EditAction.class);

    private final Supplier<Tab<?>> selectedTabSupplier;

    public EditAction(Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("name"), Icons.EDIT_ICON);
        this.selectedTabSupplier = selectedTabSupplier;
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTabSupplier.get().editSelectedRow();
    }
}
