package data;

import domain.Client;
import domain.TimeEntry;
import domain.WorkType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class TimeEntryDao extends AbstractDao<TimeEntry> {

    public TimeEntryDao(DataSource dataSource) {
        super(dataSource, "TIME_ENTRY");
    }

    @Override
    public void create(TimeEntry timeEntry) {
        if (timeEntry.getId() != null) {
            throw new IllegalArgumentException("Time entry already has ID: " + timeEntry);
        }
        String insertStatement = "INSERT INTO TIME_ENTRY (WORK_TYPE_ID, CLIENT_ID, START_TIME, END_TIME) VALUES (?, ?, ?, ?)";
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(insertStatement, RETURN_GENERATED_KEYS)) {
            st.setLong(1, timeEntry.getWorkType().getId());
            st.setLong(2, timeEntry.getClient().getId());
            st.setTimestamp(3, TimestampConverter.convertToTimeStamp(timeEntry.getStartTime()));
            st.setTimestamp(4, TimestampConverter.convertToTimeStamp(timeEntry.getEndTime()));
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    timeEntry.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for time entry: " + timeEntry);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store time entry " + timeEntry, ex);
        }
    }


    public List<TimeEntry> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "SELECT TIME_ENTRY.ID, START_TIME, END_TIME," +
                             " WORK_TYPE_ID, WORK_NAME, RATE, DESCRIPTION," +
                             " CLIENT_ID, FIRST_NAME, LAST_NAME " +
                             "FROM TIME_ENTRY " +
                             "LEFT OUTER JOIN WORK_TYPE ON WORK_TYPE_ID = WORK_TYPE.ID " +
                             "LEFT OUTER JOIN CLIENT ON CLIENT_ID = CLIENT.ID")) {

            List<TimeEntry> timeEntries = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    var workType = new WorkType(
                            rs.getString("WORK_NAME"),
                            rs.getInt("RATE"),
                            rs.getString("DESCRIPTION"));
                    workType.setId(rs.getLong("WORK_TYPE_ID"));
                    var client = new Client(
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"));
                    client.setId(rs.getLong("CLIENT_ID"));
                    TimeEntry timeEntry = new TimeEntry(
                            workType,
                            client,
                            TimestampConverter.convertToLocalDateTime(rs.getTimestamp("START_TIME")),
                            TimestampConverter.convertToLocalDateTime(rs.getTimestamp("END_TIME"))
                    );
                    timeEntry.setId(rs.getLong("ID"));
                    timeEntries.add(timeEntry);
                }
            }
            return timeEntries;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all time entries", ex);
        }
    }

    public void update(TimeEntry timeEntry) {
        if (timeEntry.getId() == null) {
            throw new IllegalArgumentException("TimeEntry has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE TIME_ENTRY SET CLIENT_ID = ?, START_TIME = ?, END_TIME = ? WHERE ID = ?")) {
            st.setLong(1, timeEntry.getClient().getId());
            st.setTimestamp(2, TimestampConverter.convertToTimeStamp(timeEntry.getStartTime()));
            st.setTimestamp(3, TimestampConverter.convertToTimeStamp(timeEntry.getEndTime()));
            st.setLong(4, timeEntry.getId());
            if (st.executeUpdate() != 1) {
                throw new DataAccessException("Failed to update non-existing workType: " + timeEntry);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete workType " + timeEntry, ex);
        }
    }
}
