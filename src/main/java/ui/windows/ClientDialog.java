package ui.windows;

import domain.Client;
import ui.I18N;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public final class ClientDialog extends EntityDialog<Client> {

    private static final ui.I18N I18N = new I18N(ClientDialog.class);

    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private Long id;


    public ClientDialog() {
        add("firstName", firstNameField);
        add("lastName", lastNameField);
    }

    @Override
    public boolean isCorrect(StringBuilder msg) {
        if (areTextFieldsEmpty(new ArrayList<>(List.of(firstNameField, lastNameField)))) {
            msg.append(I18N.getString("warningFields"));
            return false;
        }
        return true;
    }

    @Override
    public Client getEntity() {
        var client = new Client(
                firstNameField.getText(),
                lastNameField.getText());
        client.setId(id);
        return client;
    }

    @Override
    public void setEntity(Client client) {
        id = client.getId();
        firstNameField.setText(client.getFirstName());
        lastNameField.setText(client.getLastName());
    }
}
