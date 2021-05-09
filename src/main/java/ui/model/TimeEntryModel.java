package ui.model;

import data.DataAccessObject;
import domain.TimeEntry;
import ui.I18N;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


public class TimeEntryModel extends DaoTableModel<TimeEntry> {
    private static final ui.I18N I18N = new I18N(TimeEntryModel.class);

    private static final List<Column<?, TimeEntry>> COLUMNS = List.of(
            new Column<>(I18N.getString("name"), String.class, (timeEntry ->
                    timeEntry.getClient().getFirstName() + " " + timeEntry.getClient().getLastName())),
            new Column<>(I18N.getString("workType"), String.class, (timeEntry -> timeEntry.getWorkType().getName())),
            new Column<>(I18N.getString("startTime"), LocalTime.class, (timeEntry -> timeEntry.getStartTime().toLocalTime())),
            new Column<>(I18N.getString("endTime"), LocalTime.class, (timeEntry -> timeEntry.getEndTime().toLocalTime())),
            new Column<>(I18N.getString("date"), LocalDate.class, (timeEntry -> timeEntry.getEndTime().toLocalDate())),
            new Column<>(I18N.getString("duration"), Duration.class, TimeEntry::getDuration)
    );

    public TimeEntryModel(DataAccessObject<TimeEntry> timeEntryDao) {
        super(COLUMNS, timeEntryDao);
    }
}
