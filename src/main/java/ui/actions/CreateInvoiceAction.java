package ui.actions;

import data.ClientDao;
import domain.TimeEntry;
import ui.I18N;
import ui.Tab;
import ui.windows.InvoiceDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public final class CreateInvoiceAction extends AbstractAction {

    private static final ui.I18N I18N = new I18N(CreateInvoiceAction.class);
    private final ClientDao clientDao;
    private final Supplier<Tab<?>> selectedTabSupplier;

    public CreateInvoiceAction(ClientDao clientDao, Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("name"), Icons.INVOICE_ICON);
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_I);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl I"));
        this.clientDao = clientDao;
        this.selectedTabSupplier = selectedTabSupplier;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        InvoiceDialog dialog = new InvoiceDialog(clientDao, (List<TimeEntry>) selectedTabSupplier.get().getSelectedEntities());

        var parrentComponent = selectedTabSupplier.get().getTable();

        dialog.showAdd(parrentComponent).ifPresent(_component -> {
            try {
                File invoice = dialog.getEntity().createInvoice();
                if (Desktop.isDesktopSupported() && invoice.exists()) {
                    Desktop.getDesktop().open(invoice);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(selectedTabSupplier.get().getTable(), "An error occurred while writing into file");
            }
        });
    }
}
