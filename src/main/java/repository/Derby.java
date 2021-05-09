package repository;

import data.*;
import data.testDataGenerators.TestClientsGenerator;
import data.testDataGenerators.TestDataGenerator;
import data.testDataGenerators.TestTimeEntryGenerator;
import data.testDataGenerators.TestWorkTypesGenerator;
import domain.Entity;
import org.apache.derby.jdbc.EmbeddedDataSource;

import java.util.List;

public class Derby {

    private static final String DB_PATH = System.getProperty("user.home") + "/freelancer";
    private final EmbeddedDataSource dataSource;
    private final ClientService clientService;
    private final WorkTypeService workTypeService;
    private final TimeEntryService timeEntryService;

    public Derby() {
        dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName(DB_PATH);
        dataSource.setCreateDatabase("create");

        clientService = new ClientService(dataSource);
        workTypeService = new WorkTypeService(dataSource);
        timeEntryService = new TimeEntryService(dataSource);
        initAll();
    }

    private static <T extends Entity> List<T> initTable(AbstractClientService<T> service, TestDataGenerator<T> generator) {
        AbstractDao<T> abstractDao = service.getDao();
        if (abstractDao.tableExits("APP", abstractDao.getTableName())) {
            return null;
        }
        service.createTable();
        List<T> generated = generator.createTestObjects(30);
        generated.forEach(abstractDao::create);
        return generated;
    }

    private static <T extends Entity> void dropTable(AbstractClientService<T> service) {
        AbstractDao<T> abstractDao = service.getDao();
        if (abstractDao.tableExits("APP", abstractDao.getTableName())) {
            service.dropTable();
        }
    }

    private static <T extends Entity> void clearTable(AbstractClientService<T> service) {
        AbstractDao<T> abstractDao = service.getDao();
        if (abstractDao.tableExits("APP", abstractDao.getTableName())) {
            service.clearTable();
        }
    }


    public EmbeddedDataSource getDataSource() {
        return dataSource;
    }

    private void initAll() {
        var clientsGenerated = initTable(clientService, new TestClientsGenerator());
        var workTypesGenerated = initTable(workTypeService, new TestWorkTypesGenerator());
        initTable(timeEntryService, new TestTimeEntryGenerator(clientsGenerated, workTypesGenerated));
    }

    public void dropAll() {
        dropTable(timeEntryService);
        dropTable(clientService);
        dropTable(workTypeService);
    }

    public void clearAll(){
        clearTable(timeEntryService);
        clearTable(clientService);
        clearTable(workTypeService);
    }

    public ClientDao getClientDao() {
        return clientService.getDao();
    }

    public WorkTypeDao getWorkTypeDao() {
        return workTypeService.getDao();
    }

    public TimeEntryDao getTimeEntryDao() {
        return timeEntryService.getDao();
    }
}
