package ui.actions;

import ui.I18N;
import ui.Tab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public final class AddAction extends AbstractAction {

    private static final ui.I18N I18N = new I18N(AddAction.class);

    private final Supplier<Tab<?>> selectedTabSupplier;

    public AddAction(Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("name"), Icons.ADD_ICON);
        this.selectedTabSupplier = selectedTabSupplier;
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTabSupplier.get().addRow();
    }
}
