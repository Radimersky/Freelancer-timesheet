package ui;

import domain.WorkType;
import ui.model.DaoTableModel;
import ui.windows.EntityDialog;
import ui.windows.WorkTypeDialog;

import javax.swing.*;
import java.util.List;

public class WorkTypeTab extends Tab<WorkType> {

    public WorkTypeTab(DaoTableModel<WorkType> clientModel, List<Action> editActions) {
        super(clientModel, editActions);
    }

    @Override
    protected EntityDialog<WorkType> createDialog() {
        return new WorkTypeDialog();
    }
}
