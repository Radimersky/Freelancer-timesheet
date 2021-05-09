package ui.model;

import ui.I18N;

import java.util.Objects;
import java.util.function.Function;

public class Column<T, E> {
    private static final I18N I18N = new I18N(Column.class);

    private final String columnName;
    private final Class<T> columnClass;
    private final Function<E, T> valueGetter;

    public Column(String columnName, Class<T> columnClass, Function<E, T> valueGetter) {
        this.columnName = Objects.requireNonNull(columnName, "columnName");
        this.columnClass = Objects.requireNonNull(columnClass, "columnClass");
        this.valueGetter = Objects.requireNonNull(valueGetter, "valueGetter");
    }

    String getColumnName() {
        return columnName;
    }

    Class<T> getColumnClass() {
        return columnClass;
    }

    Object getValue(E entity) {
        return valueGetter.apply(entity);
    }

    void setValue(Object value, E entity) {
        throw new UnsupportedOperationException(String.format(I18N.getString("message"), columnName));
    }

    boolean isEditable() {
        return false;
    }

}
