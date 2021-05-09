package ui.windows;

import com.github.lgooddatepicker.components.DatePicker;
import data.ClientDao;
import domain.Client;
import domain.Invoice;
import domain.TimeEntry;
import ui.I18N;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

public final class InvoiceDialog extends EntityDialog<Invoice> {

    private static final ui.I18N I18N = new I18N(InvoiceDialog.class);

    private final JComboBox<Client> clientComboBox = new JComboBox<>();
    private final JComboBox<Client> issuerComboBox = new JComboBox<>();
    private final DatePicker createDatePicker;
    private final DatePicker dueDatePicker;
    private List<TimeEntry> selectedEntities;

    public InvoiceDialog(ClientDao clientDao, List<TimeEntry> selectedEntities) {
        this.selectedEntities = selectedEntities;
        var renderer = new Renderer<>();

        clientDao.findAll().forEach(client -> {
            clientComboBox.addItem(client);
            issuerComboBox.addItem(client);
        });
        clientComboBox.setRenderer(renderer);
        issuerComboBox.setRenderer(renderer);

        createDatePicker = new DatePicker();
        dueDatePicker = new DatePicker();

        add("issuer", issuerComboBox);
        add("client", clientComboBox);
        add("createDate", createDatePicker);
        add("dueDate", dueDatePicker);
    }

    @Override
    public boolean isCorrect(StringBuilder msg) {
        if (createDatePicker.getDate() == null || dueDatePicker.getDate() == null) {
            msg.append(I18N.getString("dateWarning"));
            return false;
        }

        return true;
    }

    @Override
    public Invoice getEntity() {
        var invoice = new Invoice(
                Objects.requireNonNull((Client) issuerComboBox.getSelectedItem()),
                Objects.requireNonNull((Client) clientComboBox.getSelectedItem()),
                createDatePicker.getDate(),
                dueDatePicker.getDate(),
                selectedEntities
        );
        return invoice;
    }

    @Override
    public void setEntity(Invoice invoice) {
        issuerComboBox.setSelectedItem(invoice.getIssuer());
        clientComboBox.setSelectedItem(invoice.getClient());
        createDatePicker.setDate(invoice.getCreateDate());
        dueDatePicker.setDate(invoice.getDueDate());
        selectedEntities = invoice.getTimeEntries();
    }
}
