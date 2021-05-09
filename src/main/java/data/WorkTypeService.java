package data;

import domain.WorkType;

import javax.sql.DataSource;

public class WorkTypeService extends AbstractClientService<WorkType> {

    private static final String createStatement = "CREATE TABLE APP.WORK_TYPE (" +
            "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "WORK_NAME VARCHAR(255) NOT NULL, " +
            "RATE INTEGER NOT NULL, " +
            "DESCRIPTION VARCHAR(2047) NOT NULL" +
            ")";
    private final DataSource dataSource;

    public WorkTypeService(DataSource dataSource) {
        super(dataSource, "WORK_TYPE", createStatement);
        this.dataSource = dataSource;
    }

    public WorkTypeDao getDao() {
        return new WorkTypeDao(dataSource);
    }
}