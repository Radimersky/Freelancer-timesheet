package ui.model;


import data.DataAccessObject;
import ui.I18N;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class DaoTableModel<E> extends AbstractEntityTableModel<E> {

    private static final ui.I18N I18N = new I18N(DaoTableModel.class);

    private final DataAccessObject<E> entityDao;
    private final List<E> rows = Collections.synchronizedList(new ArrayList<>());

    public DaoTableModel(List<Column<?, E>> columns, DataAccessObject<E> entityDao) {
        super(columns);
        this.entityDao = entityDao;
        load();
    }

    public void load() {
        new LoadEntitiesWorker().execute();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public E getEntity(int rowIndex) {
        return rows.get(rowIndex);
    }

    @Override
    protected void updateEntity(E entity) {
        new UpdateEntityWorker(entity).execute();
    }

    public void deleteRows(List<Integer> rowIndexes) {
        new DeleteEntitiesWorker(rowIndexes).execute();
    }

    private void deleteRow(int rowIndex) {
        rows.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(E entity) {
        new AddEntityWorker(entity).execute();
    }

    public void updateRow(E entity) {
        updateEntity(entity);
        int rowIndex = rows.indexOf(entity);
        rows.set(rowIndex, entity);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    private class LoadEntitiesWorker extends SwingWorker<Collection<E>, Void> {

        @Override
        protected Collection<E> doInBackground() {
            return entityDao.findAll();
        }

        @Override
        protected void done() {
            try {
                rows.clear();
                rows.addAll(get());
                fireTableDataChanged();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        ex.getCause().getMessage(), I18N.getString("loadingError"),
                        JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

    private class DeleteEntitiesWorker extends SwingWorker<Collection<E>, Void> {
        private final List<Integer> rowIndexes;

        private DeleteEntitiesWorker(List<Integer> rowIndexes) {
            this.rowIndexes = rowIndexes;
        }

        @Override
        protected Collection<E> doInBackground() {
            for (Integer rowIndex : rowIndexes) {
                entityDao.delete(rows.get(rowIndex));
            }
            return new ArrayList<E>();
        }

        @Override
        protected void done() {
            var iterator = rowIndexes.listIterator(rowIndexes.size());
            while (iterator.hasPrevious()) {
                deleteRow(iterator.previous());
            }
        }
    }

    private class AddEntityWorker extends SwingWorker<Collection<E>, Void> {
        private final E entity;

        private AddEntityWorker(E entity) {
            this.entity = entity;
        }

        @Override
        protected Collection<E> doInBackground() {
            entityDao.create(entity);
            return new ArrayList<E>();
        }

        @Override
        protected void done() {
            int newRowIndex = rows.size();
            rows.add(entity);
            fireTableRowsInserted(newRowIndex, newRowIndex);
        }
    }

    private class UpdateEntityWorker extends SwingWorker<Collection<E>, Void> {
        private final E entity;

        private UpdateEntityWorker(E entity) {
            this.entity = entity;
        }

        @Override
        protected Collection<E> doInBackground() {
            entityDao.update(entity);
            return new ArrayList<E>();
        }

        @Override
        protected void done() {
        }
    }

}
