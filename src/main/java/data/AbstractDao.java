package data;

import domain.Entity;

import javax.sql.DataSource;
import java.sql.SQLException;

public abstract class AbstractDao<T extends Entity> implements DataAccessObject<T>{

    protected final DataSource dataSource;
    private final String tableName;

    protected AbstractDao(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean tableExits(String schemaName, String tableName) {
        try (var connection = dataSource.getConnection();
             var rs = connection.getMetaData().getTables(null, schemaName, tableName, null)) {
            return rs.next();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to detect if the table " + schemaName + "." + tableName + " exist", ex);
        }
    }

    public abstract void create(T object);

    public void delete(T object) {
        if (object.getId() == null) {
            throw new IllegalArgumentException(tableName.toLowerCase() + " has null ID: " + object);
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("DELETE FROM " + tableName.toLowerCase() + " WHERE ID = ?")) {
            st.setLong(1, object.getId());
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted == 0) {
                throw new DataAccessException("Failed to delete non-existing " + tableName + ": " + object);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete " + tableName + " " + object, ex);
        }
    }
}
