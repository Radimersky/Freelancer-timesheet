package ui;

import domain.Invoice;
import ui.model.DaoTableModel;
import ui.windows.EntityDialog;
import ui.windows.InvoiceDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Tab<E> {

    protected final JTable table;
    protected final DaoTableModel<E> tableModel;
    private final I18N i18n = new I18N(getClass());

    public Tab(DaoTableModel<E> tableModel, List<Action> editActions) {
        this.tableModel = tableModel;
        this.table = createTable(tableModel);
        this.table.setComponentPopupMenu(createTablePopupMenu(editActions));
    }

    protected abstract EntityDialog<E> createDialog();

    String getTitle() {
        return i18n.getString("title");
    }

    JComponent getComponent() {
        return new JScrollPane(table);
    }

    public JTable getTable() {
        return table;
    }

    int getSelectedRowsCount() {
        return table.getSelectedRowCount();
    }

    void addListSelectionListener(ListSelectionListener listSelectionListener) {
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void addRow() {
        EntityDialog<E> dialog = createDialog();
        dialog.showAdd(table)
                .ifPresent(tableModel::addRow);
    }

    public void deleteSelectedRows() {
        List<Integer> modelRowIndexes = Arrays.stream(table.getSelectedRows())
                .map(table::convertRowIndexToModel)
                .boxed()
                .collect(Collectors.toList());
        tableModel.deleteRows(modelRowIndexes);
    }

    public void editSelectedRow() {
        E entity = getSelectedEntity();
        EntityDialog<E> dialog = createDialog();
        dialog.setEntity(entity);
        dialog.showEdit(table)
                .ifPresent(tableModel::updateRow);
    }

    private E getSelectedEntity() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException(i18n.getString("invalidSelected") + Arrays.toString(selectedRows));
        }
        return tableModel.getEntity(table.convertRowIndexToModel(selectedRows[0]));
    }

    public List<E> getSelectedEntities() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length < 1) {
            throw new IllegalStateException(i18n.getString("invalidSelected") + Arrays.toString(selectedRows));
        }

        List<E> entityList = new ArrayList<>();
        for (int entityIndex : selectedRows) {
            entityList.add(tableModel.getEntity(table.convertRowIndexToModel(entityIndex)));
        }
        return entityList;
    }

    private JTable createTable(TableModel tableModel) {
        var table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(20);
        table.setPreferredScrollableViewportSize(new Dimension(700, 400));
        return table;
    }

    private JPopupMenu createTablePopupMenu(List<Action> editActions) {
        var menu = new JPopupMenu();
        editActions.forEach(menu::add);
        return menu;
    }

}
