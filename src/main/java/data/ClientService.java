package data;

import domain.Client;

import javax.sql.DataSource;

public class ClientService extends AbstractClientService<Client> {

    private static final String createStatement = "CREATE TABLE APP.CLIENT (" +
            "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
            "FIRST_NAME VARCHAR(255) NOT NULL, " +
            "LAST_NAME VARCHAR(255) NOT NULL" +
            ")";

    private final DataSource dataSource;

    public ClientService(DataSource dataSource) {
        super(dataSource, "CLIENT", createStatement);
        this.dataSource = dataSource;
    }

    public ClientDao getDao() {
        return new ClientDao(dataSource);
    }

}
