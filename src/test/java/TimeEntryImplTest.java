import data.ClientDao;
import data.TimeEntryDao;
import data.WorkTypeDao;
import domain.Client;
import domain.TimeEntry;
import domain.WorkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Derby;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class TimeEntryImplTest {
    private TimeEntryDao timeEntryDao;
    private WorkTypeDao workTypeDao;
    private ClientDao clientDao;

    @BeforeEach
    public void setUpBeforeEach() {
        var derby = new Derby();
        timeEntryDao = derby.getTimeEntryDao();
        workTypeDao = derby.getWorkTypeDao();
        clientDao = derby.getClientDao();
        derby.clearAll();

    }

    private WorkType createWorkType(WorkType workType) {
        workTypeDao.create(workType);
        List<WorkType> workTypes = workTypeDao.findAll();
        for (WorkType w: workTypes) {
            if (w.equals(workType)) {
                return w;
            }
        }
        return null;
    }

    private Client createClient(Client client) {
        clientDao.create(client);
        List<Client> clients = clientDao.findAll();
        for (Client c: clients) {
            if (c.equals(client)) {
                return c;
            }
        }
        return null;
    }

    @Test
    public void addTimeEntry() {
        assertThat(timeEntryDao.findAll().size()).isEqualTo(0);

        WorkType dbWorkType = createWorkType(new WorkType("Development",50,"Some description"));
        Client dbClient = createClient(new Client("Pepa","Hájek"));

        LocalDateTime startDateTime =  LocalDateTime.of(2017, 2, 13, 15, 56);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        TimeEntry timeEntry = new TimeEntry(dbWorkType, dbClient, startDateTime, endDateTime);

        timeEntryDao.create(timeEntry);
        assertThat(timeEntryDao.findAll().size()).isEqualTo(1);

        List<TimeEntry> timeEntries = timeEntryDao.findAll();

        assertThat(timeEntries).containsOnlyOnce(timeEntry);
    }

    @Test
    public void deleteTimeEntry() {
        assertThat(timeEntryDao.findAll().size()).isEqualTo(0);

        WorkType dbWorkType = createWorkType(new WorkType("Development",50,"Some description"));
        Client dbClient = createClient(new Client("Pepa","Hájek"));

        LocalDateTime startDateTime =  LocalDateTime.of(2017, 2, 13, 15, 56);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        TimeEntry timeEntry = new TimeEntry(dbWorkType, dbClient, startDateTime, endDateTime);

        timeEntryDao.create(timeEntry);
        assertThat(timeEntryDao.findAll().size()).isEqualTo(1);

        timeEntryDao.delete(timeEntry);
        assertThat(timeEntryDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void updateClient() {
        assertThat(timeEntryDao.findAll().size()).isEqualTo(0);

        WorkType dbWorkType = createWorkType(new WorkType("Development",50,"Some description"));
        Client dbClient = createClient(new Client("Pepa","Hájek"));

        LocalDateTime startDateTime =  LocalDateTime.of(2017, 2, 13, 15, 56);
        LocalDateTime endDateTime = startDateTime.plusMinutes(30);

        TimeEntry timeEntry = new TimeEntry(dbWorkType, dbClient, startDateTime, endDateTime);
        timeEntryDao.create(timeEntry);

        startDateTime = startDateTime.plusMinutes(45);
        endDateTime = endDateTime.plusMinutes(45);

        List<TimeEntry> timeEntries = timeEntryDao.findAll();
        assertThat(timeEntries.size()).isEqualTo(1);
        TimeEntry dbTimeEntry = timeEntries.get(0);
        dbTimeEntry.setStartTime(startDateTime);
        dbTimeEntry.setEndTime(endDateTime);
        timeEntryDao.update(dbTimeEntry);

        timeEntries = timeEntryDao.findAll();
        assertThat(timeEntries.size()).isEqualTo(1);
        dbTimeEntry = timeEntries.get(0);
        assertThat(dbTimeEntry.getStartTime()).isEqualTo(startDateTime);
        assertThat(dbTimeEntry.getEndTime()).isEqualTo(endDateTime);
    }
}