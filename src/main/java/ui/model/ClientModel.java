package ui.model;

import data.DataAccessObject;
import domain.Client;
import ui.I18N;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ClientModel extends DaoTableModel<Client> {
    private static final ui.I18N I18N = new I18N(ClientModel.class);

    private static final List<Column<?, Client>> COLUMNS = List.of(
            new Column<>(I18N.getString("firstname"), String.class, Client::getFirstName),
            new Column<>(I18N.getString("surname"), String.class, Client::getLastName)
    );

    public ClientModel(DataAccessObject<Client> clientDao) {
        super(COLUMNS, clientDao);
    }
}
