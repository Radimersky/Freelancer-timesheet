package ui;

import domain.Client;
import ui.model.DaoTableModel;
import ui.windows.ClientDialog;
import ui.windows.EntityDialog;

import javax.swing.*;
import java.util.List;

public class ClientTab extends Tab<Client> {

    public ClientTab(DaoTableModel<Client> clientModel, List<Action> editActions) {
        super(clientModel, editActions);

    }

    @Override
    protected EntityDialog<Client> createDialog() {
        return new ClientDialog();
    }
}
