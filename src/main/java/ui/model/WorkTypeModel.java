package ui.model;

import data.DataAccessObject;
import domain.WorkType;
import ui.I18N;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class WorkTypeModel extends DaoTableModel<WorkType> {
    private static final ui.I18N I18N = new I18N(WorkTypeModel.class);

    private static final List<Column<?, WorkType>> COLUMNS = List.of(
            new Column<>(I18N.getString("name"), String.class, WorkType::getName),
            new Column<>(I18N.getString("rate"), Integer.class, WorkType::getRate),
            new Column<>(I18N.getString("workDesc"), String.class, WorkType::getDescription)
    );

    public WorkTypeModel(DataAccessObject<WorkType> workTypeDao) {
        super(COLUMNS, workTypeDao);
    }
}
