package data;

import domain.TimeEntry;

import javax.sql.DataSource;

public class TimeEntryService extends AbstractClientService<TimeEntry> {

    private static final String createStatement = "CREATE TABLE APP.TIME_ENTRY (" +
            "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "WORK_TYPE_ID BIGINT NOT NULL, " +
            "FOREIGN KEY (WORK_TYPE_ID) REFERENCES WORK_TYPE (ID) ON DELETE CASCADE, " +
            "CLIENT_ID BIGINT NOT NULL, " +
            "FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT (ID) ON DELETE CASCADE, " +
            "START_TIME TIMESTAMP NOT NULL," +
            "END_TIME TIMESTAMP NOT NULL" +
            ")";

    private final DataSource dataSource;

    public TimeEntryService(DataSource dataSource) {
        super(dataSource, "TIME_ENTRY", createStatement);
        this.dataSource = dataSource;
    }

    public TimeEntryDao getDao() {
        return new TimeEntryDao(dataSource);
    }
}
