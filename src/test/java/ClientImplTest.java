import data.ClientDao;
import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.Derby;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class ClientImplTest {
    private ClientDao clientDao;

    @BeforeEach
    public void setUpBeforeEach() {
        var derby = new Derby();
        derby.clearAll();
        clientDao = derby.getClientDao();
    }

    @Test
    public void addClient() {
        assertThat(clientDao.findAll().size()).isEqualTo(0);

        Client c = new Client("Pepa","Hájek");
        Client c2 = new Client("Alois","Jirásek");

        clientDao.create(c);
        assertThat(clientDao.findAll().size()).isEqualTo(1);

        clientDao.create(c2);
        List<Client> clients = clientDao.findAll();

        assertThat(clients.size()).isEqualTo(2);
        assertThat(clients).containsOnlyOnce(c);
        assertThat(clients).containsOnlyOnce(c2);
    }

    @Test
    public void deleteClient() {
        assertThat(clientDao.findAll().size()).isEqualTo(0);

        Client c = new Client("Pepa","Hájek");

        clientDao.create(c);
        assertThat(clientDao.findAll().size()).isEqualTo(1);

        clientDao.delete(c);
        assertThat(clientDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void updateClient() {
        assertThat(clientDao.findAll().size()).isEqualTo(0);

        Client c = new Client("Pepa","Hájek");

        clientDao.create(c);
        List<Client> clients = clientDao.findAll();
        assertThat(clients.size()).isEqualTo(1);

        Client dbClient = clients.get(0);
        dbClient.setFirstName("Josef");
        dbClient.setLastName("Neplecha");
        clientDao.update(dbClient);

        clients = clientDao.findAll();
        assertThat(clients.size()).isEqualTo(1);
        dbClient = clients.get(0);
        assertThat(dbClient.getFirstName()).isEqualTo("Josef");
        assertThat(dbClient.getLastName()).isEqualTo("Neplecha");
    }
}