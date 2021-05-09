package ui;


import data.ClientDao;
import data.WorkTypeDao;
import domain.TimeEntry;
import ui.model.DaoTableModel;
import ui.model.DurationRenderer;
import ui.windows.ClientDialog;
import ui.windows.EntityDialog;
import ui.windows.TimeEntryDialog;

import javax.swing.*;
import java.time.Duration;
import java.util.List;

class TimeEntryTab extends Tab<TimeEntry> {

    private final ClientDao clientDao;
    private final WorkTypeDao workTypeDao;

    private static final ui.I18N I18N = new I18N(TimeEntryTab.class);

    TimeEntryTab(DaoTableModel<TimeEntry> tableModel, List<Action> editActions,
                 ClientDao clientDao, WorkTypeDao workTypeDao) {
        super(tableModel, editActions);
        this.clientDao = clientDao;
        this.workTypeDao = workTypeDao;
        initRenderers();
    }

    private void initRenderers() {
        table.setDefaultRenderer(Duration.class, new DurationRenderer());
    }

    @Override
    protected EntityDialog<TimeEntry> createDialog() {
        return new TimeEntryDialog(clientDao, workTypeDao);
    }

    @Override
    public void addRow() {
        if (clientDao.count() == 0 ||workTypeDao.count() == 0){
            JOptionPane.showConfirmDialog(null, I18N.getString("noClientsOrWorkText"),
                    I18N.getString("noClientsOrWorkTitle"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            return;
        }
        EntityDialog<TimeEntry> dialog = createDialog();
        dialog.showAdd(table)
                .ifPresent(tableModel::addRow);
    }
}
