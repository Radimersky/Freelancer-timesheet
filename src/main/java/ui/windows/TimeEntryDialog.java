package ui.windows;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import data.ClientDao;
import data.WorkTypeDao;
import domain.Client;
import domain.TimeEntry;
import domain.WorkType;
import ui.I18N;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Objects;

public final class TimeEntryDialog extends EntityDialog<TimeEntry> {

    private static final ui.I18N I18N = new I18N(TimeEntryDialog.class);

    private final JComboBox<Client> clientComboBox = new JComboBox<>();
    private final JComboBox<WorkType> workTypeComboBox = new JComboBox<>();
    private final DatePicker datePicker;
    private final TimePicker startTimePicker;
    private final TimePicker endTimePicker;

    private Long id;

    public TimeEntryDialog(ClientDao clientDao, WorkTypeDao workTypeDao) {
        var renderer = new Renderer<>();

        clientDao.findAll().forEach(clientComboBox::addItem);
        clientComboBox.setRenderer(renderer);

        workTypeDao.findAll().forEach(workTypeComboBox::addItem);
        workTypeComboBox.setRenderer(renderer);

        datePicker = new DatePicker();
        startTimePicker = new TimePicker();
        endTimePicker = new TimePicker();

        add("client", clientComboBox);
        add("workType", workTypeComboBox);
        add("startTime", startTimePicker);
        add("endTime", endTimePicker);
        add("date", datePicker);
    }

    @Override
    public boolean isCorrect(StringBuilder msg) {
        if (startTimePicker.getTime() == null) {
            msg.append(I18N.getString("startWarning"));
            return false;
        }

        if (endTimePicker.getTime() == null) {
            msg.append(I18N.getString("endWarning"));
            return false;
        }

        if (datePicker.getDate() == null) {
            msg.append(I18N.getString("dateWarning"));
            return false;
        }

        if (startTimePicker.getTime().compareTo(endTimePicker.getTime()) >= 0) {
            msg.append(I18N.getString("invalidDuration"));
            return false;
        }
        return true;
    }

    @Override
    public TimeEntry getEntity() {
        LocalDate date = datePicker.getDate();//TODO SOLVE ID AND STORING IN DB

        var timeEntry = new TimeEntry(
                Objects.requireNonNull((WorkType) workTypeComboBox.getSelectedItem()),
                Objects.requireNonNull((Client) clientComboBox.getSelectedItem()),
                date.atTime(startTimePicker.getTime()),
                date.atTime(endTimePicker.getTime()));
        timeEntry.setId(id);
        return timeEntry;
    }

    @Override
    public void setEntity(TimeEntry timeEntry) {
        id = timeEntry.getId();
        clientComboBox.setSelectedItem(timeEntry.getClient());
        workTypeComboBox.setSelectedItem(timeEntry.getWorkType());
        datePicker.setDate(timeEntry.getStartTime().toLocalDate());
        startTimePicker.setTime(timeEntry.getStartTime().toLocalTime());
        endTimePicker.setTime(timeEntry.getEndTime().toLocalTime());
    }
}
