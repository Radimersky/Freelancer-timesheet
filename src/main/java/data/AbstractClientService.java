package data;


import domain.Entity;

import javax.sql.DataSource;
import java.sql.SQLException;

public abstract class AbstractClientService<T extends Entity> {

    private final String createStatement;
    private final DataSource dataSource;
    private final String tableName;

    public AbstractClientService(DataSource dataSource, String tableName, String createStatement) {
        this.createStatement = createStatement;
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    public abstract AbstractDao<T> getDao();

    public void createTable() {
        updateTable("create", createStatement);
    }

    public void dropTable() {
        updateTable("drop", "DROP TABLE APP." + tableName);
    }

    public void clearTable() {
        updateTable("delete content", "DELETE FROM APP." + tableName);
    }

    private void updateTable(String actionName, String sql) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(sql)) {
            st.execute();
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to " + actionName + " " + tableName + " table", ex);
        }
    }
}
